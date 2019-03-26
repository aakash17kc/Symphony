package com.logix.symphony;

public class SongClass {
    private String mSongName;
    private int mResource;

    public String getmSongName() {
        return mSongName;
    }

    public int getmResource() {
        return mResource;
    }

    public SongClass(String mSongName, int mResource) {
        this.mSongName = mSongName;
        this.mResource = mResource;
    }
}
