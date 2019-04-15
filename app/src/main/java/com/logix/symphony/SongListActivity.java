package com.logix.symphony;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.logix.symphony.Adapters.VerticalRecyclerAdapter;
import com.logix.symphony.Model.VerticalRecyclerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SongListActivity extends AppCompatActivity {

    ArrayList<SongListClass> list = new ArrayList<>();
    RecyclerView songListView;

    FirebaseFirestore firebaseFirestore;


    TextView mPlayListName;
    RecyclerView verticalRecycler;
    ArrayList<VerticalRecyclerModel> verticalList = new ArrayList<>();
    VerticalRecyclerAdapter verticalRecyclerAdapter;

    ArrayList<String> mSongCat = new ArrayList<>();
    ArrayList<String> mAlbumnames = new ArrayList<>();
    ArrayList<String> mAlbumCoverUrl = new ArrayList<>();

    HashMap<String,HashMap<String,String>> mAlbumSongMap = new HashMap<>();
    
    String mAlbumArtist;

    String albumName;
    String imagepath;
    
    


    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songListView = findViewById(R.id.albumSongList);

        firebaseFirestore = FirebaseFirestore.getInstance();





        Intent bundle = getIntent();
         albumName = bundle.getStringExtra("AlbumName");
         imagepath =  bundle.getStringExtra("Image");

        ImageView songListCover = findViewById(R.id.songListCover);
        TextView album = findViewById(R.id.albumName);

        Glide.with(this).load(imagepath).centerCrop().into(songListCover);
        album.setText(albumName);

        mFetchAlbumSongs();




    }

    private void mFetchAlbumSongs() {


        documentReference = firebaseFirestore.document("AllSongs/"+albumName);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mAlbumSongMap = new HashMap<>();
                mAlbumArtist = (String) documentSnapshot.get("Artist");                
                mAlbumSongMap = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("SongList");
                if (mAlbumSongMap != null ) {

                    mPopulateSongList();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SongListActivity.this," "+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("FAILURE",""+e.getMessage());
            }
        });
        

    }

    private void mPopulateSongList() {

        Iterator<Map.Entry<String, HashMap<String, String>>> songListParent = mAlbumSongMap.entrySet().iterator();

        while (songListParent.hasNext()){
            Map.Entry<String, HashMap<String, String>> parentPair = songListParent.next();

            Iterator<Map.Entry<String, String>> child = (parentPair.getValue()).entrySet().iterator();

            if ((child.hasNext())){
                Map.Entry childPair = child.next();
                list.add(new SongListClass(parentPair.getKey(), (String) child.next().getValue(),imagepath));
                 child.remove();
            }

        }

        songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        SongListAdapter songListAdapter = new SongListAdapter(this,list);

        songListView.setAdapter(songListAdapter);


    }


}
