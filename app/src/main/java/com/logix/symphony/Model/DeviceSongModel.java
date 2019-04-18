package com.logix.symphony.Model;

public class DeviceSongModel {

    private String mSongName;
    private int mResource;
    private String path;
    private String artist;

    public String getPath() {
        return path;
    }

    public String getmSongName() {
        return mSongName;
    }

    public int getmResource() {
        return mResource;
    }
    public String getArtist(){
        return artist;
    }

    public DeviceSongModel(String artist, String mSongName, String path) {
        this.mSongName = mSongName;
        this.path = path;
        this.artist = artist;
    }

}
