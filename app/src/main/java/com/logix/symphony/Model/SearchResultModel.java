package com.logix.symphony.Model;

public class SearchResultModel {

    public String getmSongName() {
        return mSongName;
    }

    public String getmSongArtist() {
        return mSongArtist;
    }

    public String getmSongImage() {
        return mSongImage;
    }

    String mSongName;
    String mSongArtist;
    String mSongImage;

  public   SearchResultModel(){

    }


    public SearchResultModel(String mSongName, String mSongArtist, String mSongImage) {
        this.mSongName = mSongName;
        this.mSongArtist = mSongArtist;
        this.mSongImage = mSongImage;
    }

}
