package com.logix.symphony.Model;

public class HorizontalRecylerModel {

    private String mSongName;
    private int mResource;

    public String getmSongName() {
        return mSongName;
    }

    public int getmResource() {
        return mResource;
    }

    public HorizontalRecylerModel(String mSongName, int mResource) {
        this.mSongName = mSongName;
        this.mResource = mResource;
    }
}
