package com.logix.symphony.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.logix.symphony.CurrentSongActivity;
import com.logix.symphony.HomeFragment;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.R;
import com.logix.symphony.Model.VerticalRecyclerModel;
import com.logix.symphony.RecyclerItemClickListener;

import java.util.ArrayList;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<VerticalRecyclerAdapter.VerticalItemHolder> {

    Context context;
    ArrayList<VerticalRecyclerModel> list;
    HomeFragment homeFragment = new HomeFragment();

    ImageView imageView;
    TextView textView;
    RecyclerView recyclerView;
    private boolean flag=false;
    AdapterClickInterface adapterClickInterface;


    public VerticalRecyclerAdapter(Context context, ArrayList<VerticalRecyclerModel> list,HomeFragment homeFragment, AdapterClickInterface adapterClickInterface) {
        this.context = context;
        this.list = list;
        this.adapterClickInterface = adapterClickInterface;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public VerticalItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.vertical_album_model,viewGroup,false);
        recyclerView  = view.findViewById(R.id.recyclerHorizontal);

        flag = false;



        return new VerticalItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VerticalItemHolder verticalItemHolder, int i) {

        VerticalRecyclerModel vrm = list.get(i);

        verticalItemHolder.header.setText(vrm.getHeader());
        ArrayList<HorizontalRecylerModel> albums = vrm.getList();


        HorizontalRecyclerAdapter horizontalRecyclerAdapter = new HorizontalRecyclerAdapter(context,albums,homeFragment,adapterClickInterface);

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
