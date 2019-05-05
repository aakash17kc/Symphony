package com.logix.symphony;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.logix.symphony.Model.OfflineSongsList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class OfflineSongListActivity extends AppCompatActivity {

    ArrayList<OfflineSongsList> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_song_list);
        
        String root = Environment.getRootDirectory()+"/Symphony/Downloads";

       // ArrayList<HashMap<String,String>> songList=
        songsList = new ArrayList<>();

        getPlayList(root);




    }

    ArrayList<HashMap<String,String>> getPlayList(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    HashMap<String, String> song = new HashMap<>();
                    songsList.add(new OfflineSongsList(file.getAbsolutePath(),file.getName()));
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }



}
