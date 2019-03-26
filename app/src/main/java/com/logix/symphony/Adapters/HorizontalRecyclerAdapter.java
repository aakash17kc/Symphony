package com.logix.symphony.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.R;

import java.util.ArrayList;

public class HorizontalRecyclerAdapter  extends RecyclerView.Adapter<HorizontalRecyclerAdapter.HorizontalRecyclerHolder> {

    Context context;
    ArrayList<HorizontalRecylerModel> list;

    public HorizontalRecyclerAdapter(Context context, ArrayList<HorizontalRecylerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HorizontalRecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.horizontal_recyler_model,viewGroup,false);
        return new HorizontalRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalRecyclerHolder horizontalRecyclerHolder, int i) {

        HorizontalRecylerModel hrm = list.get(i);
        horizontalRecyclerHolder.thumbanail.setImageResource(hrm.getmResource());
        horizontalRecyclerHolder.songName.setText(hrm.getmSongName());

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
