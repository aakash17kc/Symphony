package com.logix.symphony;

public class SongListClass {


    public SongListClass(String songName, String artistName, String image) {
        this.songName = songName;
        this.artistName = artistName;
        this.image = image;
    }

    String songName,artistName;
    String image;

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getImage() {
        return image;
    }

}
