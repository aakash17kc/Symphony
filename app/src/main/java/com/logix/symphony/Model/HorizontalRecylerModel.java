package com.logix.symphony.Model;

public class  HorizontalRecylerModel{

    private String mSongName;
    private String mResource;

    public String getmSongName() {
        return mSongName;
    }

    public String getmResource() {
        return mResource;
    }

    public HorizontalRecylerModel(String mSongName, String mResource) {
        this.mSongName = mSongName;
        this.mResource = mResource;
    }
}
