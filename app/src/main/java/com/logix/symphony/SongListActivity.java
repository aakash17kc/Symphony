package com.logix.symphony;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity {

    ArrayList<SongListClass> list = new ArrayList<>();
    RecyclerView songListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songListView = findViewById(R.id.albumSongList);



        for(int i=0;i<10;i++){
            list.add(new SongListClass("Dancing With Strnagers by Sam Smith and Normani","Sam Smith, Normanni",R.drawable.normani));
        }

        songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        SongListAdapter songListAdapter = new SongListAdapter(this,list);

        songListView.setAdapter(songListAdapter);

    }
}
