package com.logix.symphony;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logix.symphony.Interfaces.AdapterClickInterface;

import java.util.ArrayList;
import java.util.Stack;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListHolder> {

    Context context;
    ArrayList<SongListClass> list = new ArrayList<>();
    int row_index=-1;
    BottomSheetSongOption bottomSheetSongOption;
    FragmentManager fragmentManager;
    AdapterClickInterface adapterClickInterface;
    Bundle songbundle;
    ArrayList<Audio> audioArrayList;

    SongListAdapter(Context context, ArrayList<SongListClass> list, ArrayList<Audio> audioList, FragmentManager fragmentManager, AdapterClickInterface adapterClickInterface){
        this.context = context;
        this.list  = list;
        this.fragmentManager = fragmentManager;
        this.adapterClickInterface = adapterClickInterface;
        this.audioArrayList = audioList;
    }

    @NonNull
    @Override
    public SongListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view  = LayoutInflater.from(context).inflate(R.layout.album_list_layout,viewGroup,false);
       view.setSelected(true);
       return new SongListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SongListHolder songListHolder, final int i) {
        final SongListClass songListClass = list.get(i);
        final Audio song = audioArrayList.get(i);
        ImageView image = songListHolder.image;
        Glide.with(context).load(songListClass.getImage()).centerCrop().into(image);


        songListHolder.songName.setText(songListClass.getSongName());
        songListHolder.artistName.setText(songListClass.getArtistName());

        songListHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onAdapterClick(songListClass.getmSongUrl(),""+i);
               // songListHolder.albumName.setTextColor(Color.parseColor("#00574B"));
                row_index = i;
                notifyDataSetChanged();


            }
        });

        if(row_index==i){
             songListHolder.songName.setTextColor(Color.parseColor("#73DF00"));

        }
        else {
            songListHolder.songName.setTextColor(Color.parseColor("#ffffff"));

        }
        songListHolder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            bottomSheetSongOption = new BottomSheetSongOption();
                songbundle = new Bundle();
                songbundle.putString("SongUrl",song.getData());
                songbundle.putString("SongName",songListClass.getSongName());
                bottomSheetSongOption.setArguments(songbundle);

                bottomSheetSongOption.show(fragmentManager,"BOTTOMSHEET");



                
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SongListHolder extends RecyclerView.ViewHolder {

        TextView songName,artistName;
        ImageView image;
        ImageView options;
        LinearLayout linearLayout;
        public SongListHolder(@NonNull View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.songListSongName);
            artistName = itemView.findViewById(R.id.songListSongArtist);
            artistName.setSelected(true);
            image = itemView.findViewById(R.id.songListSongImage);
            options = itemView.findViewById(R.id.songOptions);
            linearLayout = itemView.findViewById(R.id.songLinear);

        }
    }
}
