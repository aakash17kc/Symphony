package com.logix.symphony;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import static com.logix.symphony.OnDeviceSongActivity.Broadcast_PLAY_NEW_AUDIO;

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
    ArrayList<SongListClass> mSongList = new ArrayList<>();

    HashMap<String,HashMap<String,String>> mAlbumSongMap = new HashMap<>();
    
    String mAlbumArtist;


    boolean audioserviceBound = false;
    boolean bufferaudioserviceBound = false;


    String albumName;
    String imagepath;
    ArrayList<Audio> audioList;

    private MediaPlayerService audioplayer;
    private AudioStreamMediaService bufferAudioplayer;
    Intent audioplayerIntent ;
    Intent bufferplayerIntent;
    int mMode = 0;
    
    


    DocumentReference documentReference;
    private HashMap<String, HashMap<String, String>> temphashmap;
    private ImageView mImageResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songListView = findViewById(R.id.albumSongList);

        firebaseFirestore = FirebaseFirestore.getInstance();

        audioplayerIntent = new Intent(this, MediaPlayerService.class);
         bufferplayerIntent = new Intent(this,AudioStreamMediaService.class);



        Intent bundle = getIntent();
         albumName = bundle.getStringExtra("AlbumName");
         imagepath =  bundle.getStringExtra("Image");

        ImageView songListCover = findViewById(R.id.songListCover);
        TextView album = findViewById(R.id.albumName);

        Glide.with(this).load(imagepath).centerCrop().into(songListCover);
        album.setText(albumName);

        mMode = bundle.getIntExtra("Interaction",0);
        if(mMode==00) {

            mFetchAlbumSongs();
           // initRecyclerView();
        }else if(mMode==01){
            loadAudio(albumName);
            initRecyclerView();
        }




    }

    private void initRecyclerView() {
        if (mSongList.size() > 0) {
            songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            SongListAdapter songListAdapter = new SongListAdapter(this,mSongList);

            songListView.setAdapter(songListAdapter);
            songListView.addOnItemTouchListener(new RecyclerItemTouchListener(this, new onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    playAudio(index);
                }
            }));

        }
    }


    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!audioserviceBound) {
            if(bufferaudioserviceBound){
                stopService(bufferplayerIntent);
            }
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            audioplayerIntent = new Intent(this, MediaPlayerService.class);
            audioplayerIntent.putExtra("AlbumImage",imagepath);
            startService(audioplayerIntent);
            bindService(audioplayerIntent, audioserviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("AlbumImage",imagepath);
            sendBroadcast(broadcastIntent);
        }
    }

    private void bufferAudio(int audioIndex){
        //Check is service is active
        if (!bufferaudioserviceBound) {
           if(audioserviceBound){
               stopService(audioplayerIntent);
           }
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            bufferplayerIntent.putExtra("AlbumImage",imagepath);
            startService(bufferplayerIntent);
            bindService(bufferplayerIntent, bufferServiceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new audioIndex to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudioIndex(audioIndex);

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            broadcastIntent.putExtra("AlbumImage",imagepath);
            sendBroadcast(broadcastIntent);
        }
    }


    private void loadAudio(String mAlbumName) {
        ContentResolver contentResolver = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection =MediaStore.Audio.Media.ALBUM + "=?";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String [] selectionargs = {mAlbumName};
        String[] projection ={MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.DISPLAY_NAME,MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST };
        Cursor cursor = (Cursor) contentResolver.query(uri, projection, selection, selectionargs, null);

        if (cursor != null && cursor.getCount() > 0) {
            audioList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList


                audioList.add(new Audio(data, title, album, artist));

                mSongList.add(new SongListClass(title,artist,imagepath,""));
            }
        }
        cursor.close();
    }


    private void mFetchAlbumSongs() {


        documentReference = firebaseFirestore.document("AllSongs/"+albumName);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mAlbumSongMap = new HashMap<>();
                mAlbumArtist = (String) documentSnapshot.get("Artist");                
                mAlbumSongMap = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("SongList");
                Log.i("SONGLIST ",albumName+" "+String.valueOf(mAlbumSongMap));
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

    /*private void temp() {

        documentReference = firebaseFirestore.document("AllSongs/Essentials");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                temphashmap = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("SongList");
                Toast.makeText(SongListActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();

                Log.i("SUCCESS ", String.valueOf(temphashmap));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SongListActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("on failure ", e.getMessage());

            }
        });
    }*/



    private void mPopulateSongList() {
        audioList = new ArrayList<>();

        Iterator<Map.Entry<String, HashMap<String, String>>> songListParent = mAlbumSongMap.entrySet().iterator();

        while (songListParent.hasNext()){
            Map.Entry<String, HashMap<String, String>> parentPair = songListParent.next();

            Iterator<Map.Entry<String, String>> child = (parentPair.getValue()).entrySet().iterator();

            //Log.i("CHECK THE CHILD",child.next().getValue());
            String artist = "";
            String songName = parentPair.getKey();


            while ((child.hasNext())){
                Map.Entry childPair = child.next();
                String songurl = " ";
               // Log.i("TYPE",c)

                if(childPair.getKey().equals("Artist")){
                    list.add(new SongListClass(parentPair.getKey(), (String) childPair.getValue(),imagepath,""));
                    artist = childPair.getValue().toString();

                }
                if(childPair.getKey().equals("SongUrl")){
                    songurl = (String) childPair.getValue();
                    audioList.add(new Audio(songurl, songName, albumName, artist));
                    Log.i("SONGLIST", ""+songName+" : "+songurl);


                }

                //list.add(new SongListClass(parentPair.getKey(), (String) childPair.getValue(),imagepath,""));
                 /*if(childPair.getKey().equals("Artist") && child.hasNext()){
                     list.add(new SongListClass(parentPair.getKey(), (String) childPair.getValue(),imagepath,""));

                     childPair = child.next();
                   songurl = (String) childPair.getValue();
                     audioList.add(new Audio(songurl, songName, albumName, artistName));
                 }
                 else if (childPair.getKey().equals("SongUrl")){
                     songurl = (String) childPair.getValue();
                     audioList.add(new Audio(songurl, songName, albumName, artistName));
                 }*/
            }
          /*  String songName = parentPair.getKey();
            String artistName = "";
            String songurl = " ";

            while (child.hasNext()){
                Map.Entry childPair = child.next();
                String key = (String) childPair.getKey();
                String value = (String) childPair.getValue();

                if(key.equals("Artist")){
                    artistName = (String) childPair.getValue();
                    songurl = " ";
                    list.add(new SongListClass(parentPair.getKey(), value,imagepath,""));
                }
                else  if (key.equals("SongUrl")){
                    songurl = value;
                    audioList.add(new Audio(songurl, songName, albumName, artistName));

                }

            }*/


        }

        songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        SongListAdapter songListAdapter = new SongListAdapter(this,list);

        songListView.setAdapter(songListAdapter);
        songListView.addOnItemTouchListener(new RecyclerItemTouchListener(this, new onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                bufferAudio(index);
            }
        }));


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("serviceStatus", audioserviceBound);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        audioserviceBound = savedInstanceState.getBoolean("serviceStatus");
    }

    //Binding this Client to the AudioPlayer Service
    private ServiceConnection audioserviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
            audioplayer = binder.getService();
            audioserviceBound = true;
        }



        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioserviceBound = false;
        }
    };

    private ServiceConnection bufferServiceConnection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioStreamMediaService.LocalBinder binder = (AudioStreamMediaService.LocalBinder) service;
            bufferAudioplayer = binder.getService();
            bufferaudioserviceBound =true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bufferaudioserviceBound = false;

        }
    };




}
