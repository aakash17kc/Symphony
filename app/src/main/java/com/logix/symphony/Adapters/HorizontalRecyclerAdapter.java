package com.logix.symphony.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.logix.symphony.HomeFragment;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.R;

import java.util.ArrayList;

public class HorizontalRecyclerAdapter  extends RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalRecyclerHolder> {

    Context context;
    ArrayList<HorizontalRecylerModel> list;
    HomeFragment homeFragment;

    int image = 0;
    String mAlbumName;

    ImageView imageView;
    TextView textView;
    AdapterClickInterface adapterClickInterface;


    public HorizontalRecyclerAdapter(Context context, ArrayList<HorizontalRecylerModel> list,HomeFragment homeFragment, AdapterClickInterface adapterClickInterface) {
        this.context = context;
        this.list = list;
        this.adapterClickInterface = adapterClickInterface;
        this.homeFragment = homeFragment;
    }


    @NonNull
    @Override
    public HorizontalRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.horizontal_recyler_model,viewGroup,false);
        imageView = view.findViewById(R.id.songThumbnail);
        textView = view.findViewById(R.id.songName);


        return new HorizontalRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalRecyclerHolder horizontalRecyclerHolder, int i) {

        final HorizontalRecylerModel hrm = list.get(i);
        ImageView thumbnail = horizontalRecyclerHolder.thumbanail;
       // horizontalRecyclerHolder.thumbanail.setImageBitmap(BitmapFactory.decodeFile(hrm.getmResource()));

        Glide.with(context).load(hrm.getmResource()).centerCrop().into(thumbnail);
        horizontalRecyclerHolder.songName.setText(hrm.getmSongName());

        horizontalRecyclerHolder.thumbanail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onAdapterClick(hrm.getmSongName(),hrm.getmResource());

            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HorizontalRecyclerHolder extends RecyclerView.ViewHolder {
        ImageView thumbanail;
        TextView songName;

        public HorizontalRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            thumbanail = itemView.findViewById(R.id.songThumbnail);
            songName = itemView.findViewById(R.id.songName);
        }
    }


}
