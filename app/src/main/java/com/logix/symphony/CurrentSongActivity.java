package com.logix.symphony;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrentSongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);

        String songName = getIntent().getStringExtra("SongName");
        TextView textView = findViewById(R.id.playing_song_name);
        textView.setText(songName);


    }
}
