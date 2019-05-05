package com.logix.symphony;


import java.io.Serializable;

public class Audio implements Serializable {

    private String data;
    private String title;

    private String album;
    private String artist;

    public String getImage() {
        return image;
    }

    private String image;

    public Audio(String data, String title, String album, String artist, String image) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.image = image;
    }


    public Audio(String data, String title, String album, String artist) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
    }

    public String getData() {
        return data;
    }


    public String getTitle() {
        return title;
    }


    public String getAlbum() {
        return album;
    }


    public String getArtist() {
        return artist;
    }



}
