package com.logix.symphony.Model;

public class BrowseDataModel {

    public BrowseDataModel(int image, String playlistnName) {
        this.image = image;
        this.playlistnName = playlistnName;
    }

    int image;
    String playlistnName;

    public int getImage() {
        return image;
    }

    public String getPlaylistnName() {
        return playlistnName;
    }
}
