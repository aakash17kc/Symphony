package com.logix.symphony;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListHolder> {

    Context context;
    ArrayList<SongListClass> list = new ArrayList<>();

    SongListAdapter(Context context, ArrayList<SongListClass> list){
        this.context = context;
        this.list  = list;
    }

    @NonNull
    @Override
    public SongListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view  = LayoutInflater.from(context).inflate(R.layout.album_list_layout,viewGroup,false);
       return new SongListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListHolder songListHolder, int i) {
        SongListClass songListClass = list.get(i);
        songListHolder.image.setImageResource(songListClass.getImage());
        songListHolder.songName.setText(songListClass.getSongName());
        songListHolder.artistName.setText(songListClass.getArtistName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SongListHolder extends RecyclerView.ViewHolder {

        TextView songName,artistName;
        ImageView image;
        public SongListHolder(@NonNull View itemView) {
            super(itemView);

            songName = itemView.findViewById(R.id.songListSongName);
            artistName = itemView.findViewById(R.id.songListSongArtist);
            image = itemView.findViewById(R.id.songListSongImage);

        }
    }
}
