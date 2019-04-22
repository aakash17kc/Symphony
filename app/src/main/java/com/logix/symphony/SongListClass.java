package com.logix.symphony;

public class SongListClass {


    public SongListClass(String songName, String artistName, String image,String mSongUrl) {
        this.songName = songName;
        this.artistName = artistName;
        this.image = image;
        this.mSongUrl = mSongUrl;
    }

   private String songName,artistName;
   private String image;

    public String getmSongUrl() {
        return mSongUrl;
    }

    private String mSongUrl;

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
