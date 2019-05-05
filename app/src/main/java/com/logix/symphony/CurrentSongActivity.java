package com.logix.symphony;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CurrentSongActivity extends AppCompatActivity {

    String songName, artistName,image,albumName;

    TextView song,artist,album;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_song);


        Intent bundle = getIntent();
        songName = bundle.getStringExtra("SongName");
        artistName = bundle.getStringExtra("Artist");
        image = bundle.getStringExtra("Image");
        albumName = bundle.getStringExtra("AlbumName");

        song = findViewById(R.id.playing_song_name);
        artist = findViewById(R.id.playing_song_artists);
        album = findViewById(R.id.albumName);
        imageView = findViewById(R.id.playing_song_image);

        song.setText(songName);
        artist.setText(artistName);
        Glide.with(this).load(image).centerCrop().into(imageView);
        album.setText(albumName);










    }
}
