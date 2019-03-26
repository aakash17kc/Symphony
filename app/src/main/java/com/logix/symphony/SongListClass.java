package com.logix.symphony;

public class SongListClass {


    public SongListClass(String songName, String artistName, int image) {
        this.songName = songName;
        this.artistName = artistName;
        this.image = image;
    }

    String songName,artistName;
    int image;

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getImage() {
        return image;
    }

}
