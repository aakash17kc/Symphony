package com.logix.symphony.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.R;
import com.logix.symphony.Model.VerticalRecyclerModel;

import java.util.ArrayList;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.VerticalItemHolder> {

    Context context;
    ArrayList<VerticalRecyclerModel> list;

    public VerticalRecyclerAdapter(Context context, ArrayList<VerticalRecyclerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VerticalItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.vertical_album_model,viewGroup,false);
        return new VerticalItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalItemHolder verticalItemHolder, int i) {

        VerticalRecyclerModel vrm = list.get(i);

        verticalItemHolder.header.setText(vrm.getHeader());
        ArrayList<HorizontalRecylerModel> albums = vrm.getList();


        HorizontalRecyclerAdapter horizontalRecyclerAdapter = new HorizontalRecyclerAdapter(context,albums);

        verticalItemHolder.albumList.setHasFixedSize(true);
        verticalItemHolder.albumList.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        verticalItemHolder.albumList.setAdapter(horizontalRecyclerAdapter);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class VerticalItemHolder extends RecyclerView.ViewHolder {

        TextView header;
        RecyclerView albumList;

        public VerticalItemHolder(@NonNull View itemView) {
            super(itemView);

            header = itemView.findViewById(R.id.playlistName);
            albumList = itemView.findViewById(R.id.recyclerHorizontal);
        }
    }
}
