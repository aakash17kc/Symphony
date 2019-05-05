package com.logix.symphony.Model;

public class OfflineSongsList {

    String songPath;

    public String getSongPath() {
        return songPath;
    }

    public String getSoongName() {
        return soongName;
    }

    String soongName;


    public OfflineSongsList(String songPath, String soongName) {
        this.songPath = songPath;
        this.soongName = soongName;
    }

}
