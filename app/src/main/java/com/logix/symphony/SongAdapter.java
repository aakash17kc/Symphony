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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    Context context;
    ArrayList<SongClass> list;

    public SongAdapter(Context context, ArrayList<SongClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view =LayoutInflater.from(context).inflate(R.layout.thumbnail,viewGroup,false);
        return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder viewHolder, int i) {

        SongClass songClass = list.get(i);

        viewHolder.thumbanail.setImageResource(songClass.getmResource());
        viewHolder.songName.setText(songClass.getmSongName());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {

        ImageView thumbanail;
        TextView songName;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            thumbanail = itemView.findViewById(R.id.songThumbnail);
            songName = itemView.findViewById(R.id.songName);
        }
    }
}
