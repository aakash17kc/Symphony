package com.logix.symphony.Model;

public class BrowseDataModel {

    public BrowseDataModel(String image, String playlistnName) {
        this.image = image;
        this.playlistnName = playlistnName;
    }

    String image;
    String playlistnName;

    public String getImage() {
        return image;
    }

    public String getPlaylistnName() {
        return playlistnName;
    }
}
