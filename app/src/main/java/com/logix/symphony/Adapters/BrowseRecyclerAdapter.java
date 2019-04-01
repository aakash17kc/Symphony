package com.logix.symphony.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logix.symphony.Model.BrowseDataModel;
import com.logix.symphony.R;

import java.util.ArrayList;

public class BrowseRecyclerAdapter  extends RecyclerView.Adapter<BrowseRecyclerAdapter.BrowseDataHolder> {

    Context context;
    ArrayList<BrowseDataModel> list;

    ImageView imageView;
    TextView textView;

    public BrowseRecyclerAdapter(Context context, ArrayList<BrowseDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BrowseDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.browse_cardview,viewGroup,false);


        return new BrowseDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseDataHolder browseDataHolder, int i) {
        BrowseDataModel browseDataModel = list.get(i);

        browseDataHolder.imageView.setImageResource(browseDataModel.getImage());
        browseDataHolder.textView.setText(browseDataModel.getPlaylistnName());

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
