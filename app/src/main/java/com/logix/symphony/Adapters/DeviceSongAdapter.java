package com.logix.symphony.Adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.DeviceSongModel;
import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.R;

import java.util.ArrayList;

public class DeviceSongAdapter extends RecyclerView.Adapter<DeviceSongAdapter.DeviceSongHolder> {

    Context context;
    ArrayList<DeviceSongModel> albumlist;
    AdapterClickInterface adapterClickInterface;


    public DeviceSongAdapter(Context context, ArrayList<DeviceSongModel> albumlist, AdapterClickInterface adapterClickInterface) {
        this.context = context;
        this.albumlist = albumlist;
        this.adapterClickInterface = adapterClickInterface;
    }



    @NonNull
    @Override
    public DeviceSongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_song_cardview,viewGroup,false);
        return new DeviceSongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceSongHolder deviceSongHolder, int i) {
        final DeviceSongModel hrm = albumlist.get(i);
        deviceSongHolder.imageView.setImageBitmap(BitmapFactory.decodeFile(hrm.getPath()));
        deviceSongHolder.textView.setText(hrm.getmSongName());
        deviceSongHolder.artist.setText(hrm.getArtist());
        deviceSongHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterClickInterface.onAdapterClick(hrm.getmSongName(),hrm.getPath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumlist.size();
    }

    public class DeviceSongHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView artist;


        public DeviceSongHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.device_song_album_image);
            textView = (TextView) itemView.findViewById(R.id.device_song_album_name);
            artist = itemView.findViewById(R.id.device_song_album_artist);
        }
    }
}
