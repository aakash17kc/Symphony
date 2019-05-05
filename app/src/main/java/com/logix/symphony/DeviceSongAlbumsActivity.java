package com.logix.symphony;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.logix.symphony.Adapters.DeviceSongAdapter;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.DeviceSongModel;

import java.util.ArrayList;

public class DeviceSongAlbumsActivity extends AppCompatActivity {

    public static String Broadcast_PLAY_NEW_AUDIO = "om.logix.symphony.PlayNewAudio" ;

    private ArrayList<DeviceSongModel> audioList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_device_song);

        int mode = getIntent().getIntExtra("Interaction",00);


            loadAudio(null);

        initRecyclerView();
    }


    private void loadAudio(String downloadpath) {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        String selection =downloadpath;
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String [] projection = { MediaStore.Audio.Albums._ID,MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.ARTIST };
        String albumId = MediaStore.Audio.Albums.ALBUM_ID;
        Cursor cursor;
             cursor = (Cursor) contentResolver.query(uri, projection, null, null, null);



        if (cursor != null && cursor.getCount() > 0) {
            Toast.makeText(DeviceSongAlbumsActivity.this," inside load",Toast.LENGTH_SHORT).show();

            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                //String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                //String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                //int albumImageUrl = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                // Toast.makeText(AlbumActivity.this," "+album,Toast.LENGTH_SHORT).show();

                // Save to audioList
                audioList.add(new DeviceSongModel(artist,album,path));
            }
        }
        cursor.close();
    }

    private void initRecyclerView() {
        if (audioList.size() > 0) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.onDeviceSongRecyler);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            DeviceSongAdapter adapter = new DeviceSongAdapter(this, audioList, new AdapterClickInterface() {
                @Override
                public void onAdapterClick(String hrm, String imagePath) {
                    Toast.makeText(DeviceSongAlbumsActivity.this," "+hrm,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeviceSongAlbumsActivity.this,SongListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("AlbumName",hrm);
                    bundle.putString("Image",imagePath);
                    bundle.putInt("Interaction",01);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);


        }
    }

}
