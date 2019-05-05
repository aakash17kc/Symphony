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
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.BrowseDataModel;
import com.logix.symphony.Model.SearchResultModel;
import com.logix.symphony.R;

import java.util.ArrayList;

public class BrowseRecyclerAdapter  extends RecyclerView.Adapter<BrowseRecyclerAdapter.BrowseDataHolder> {

    Context context;
    ArrayList<SearchResultModel> list;

    ImageView imageView;
    TextView textView;
    AdapterClickInterface adapterClickInterface;

    public BrowseRecyclerAdapter(Context context, ArrayList<SearchResultModel> list, AdapterClickInterface adapterClickInterface) {
        this.context = context;
        this.list = list;
        this.adapterClickInterface = adapterClickInterface;
    }

    @NonNull
    @Override
    public BrowseDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.browse_cardview,viewGroup,false);

        return new BrowseDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseDataHolder browseDataHolder, int i) {
        final SearchResultModel searchResultModel = list.get(i);

        ImageView imageView = browseDataHolder.imageView;
        Glide.with(context).load(searchResultModel.getmSongImage()).centerCrop().into(imageView);

        browseDataHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickInterface.onAdapterClick(searchResultModel.getmSongName(),searchResultModel.getmSongImage());
            }
        });

        browseDataHolder.textView.setText(searchResultModel.getmSongArtist());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BrowseDataHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public BrowseDataHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.browse_recycler_image);
            textView = itemView.findViewById(R.id.browse_recycler_text);

        }
    }
}
