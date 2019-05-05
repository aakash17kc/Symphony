package com.logix.symphony;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.logix.symphony.Adapters.VerticalRecyclerAdapter;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.VerticalRecyclerModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.logix.symphony.DeviceSongAlbumsActivity.Broadcast_PLAY_NEW_AUDIO;

public class SongListActivity extends AppCompatActivity implements BottomSheetSongOption.BottomSheetListener {

    ArrayList<SongListClass> list = new ArrayList<>();
    RecyclerView songListView;

    FirebaseFirestore firebaseFirestore;

    ArrayList<String> songNames = new ArrayList<>();


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

    String songUrlDownload,songNameDownload;


    boolean audioserviceBound = false;
    boolean bufferaudioserviceBound = false;


    String albumName;
    String imagepath;
    ArrayList<Audio> audioList;

    private MediaPlayerService audioplayer;
    Intent audioplayerIntent ;
    Intent bufferplayerIntent;
    int mMode = 0;
    
    


    DocumentReference documentReference;
    private HashMap<String, HashMap<String, String>> temphashmap;
    private ImageView mImageResource;
    private boolean flag;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String mUserName;
    TextView mName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songListView = findViewById(R.id.albumSongList);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mName = findViewById(R.id.albumMadeForUser);


        if(mUser!=null){
            mUserName = "MADE FOR "+mUser.getDisplayName().toUpperCase();
        }


        mName.setText(mUserName);

        firebaseFirestore = FirebaseFirestore.getInstance();

        audioplayerIntent = new Intent(this, MediaPlayerService.class);



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


       // saveToDownloadDirectory();




    }


    private void saveToDownloadDirectory(String songurl, String songname) {

        try {
            if (flag) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // Toast.makeText(this, "Space available", Toast.LENGTH_SHORT).show();
                    File root = Environment.getExternalStorageDirectory();

                    //for private files. deleted after app uninstall
                    File privateroot = this.getExternalFilesDir(null);

                    File dir = new File(root + "/Symphony/Downloads");


                    if (!dir.exists()) {

                        if (dir.mkdirs()) {
                            Toast.makeText(this, " Folder created", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, " Folder not created", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        //Toast.makeText(this, " Folder exits", Toast.LENGTH_SHORT).show();

                    }
                    String filepath = "/Symphony/Downloads";

                 //   Toast.makeText(this, " " + filepath, Toast.LENGTH_SHORT).show();

                    Toast.makeText(this, " Downloading ", Toast.LENGTH_SHORT).show();


                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    Uri uri = Uri.parse(songurl);
                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(filepath, songname + ".mp3");
                    // request.setDestinationInExternalFilesDir(SongListActivity.this,filepath,songname+".mp3");
                    downloadManager.enqueue(request);


                }
            }
        }
        catch (Exception e){
            Toast.makeText(SongListActivity.this,"File is already Offline",Toast.LENGTH_SHORT).show();
        }


    }

    public  void checkStoreagePersmission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                saveToDownloadDirectory(songUrlDownload,songNameDownload);
                flag = true;

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                saveToDownloadDirectory(songUrlDownload,songNameDownload);
                flag = true;
            }
            else {
                Toast.makeText(this, "Denied " + permissions[0], Toast.LENGTH_SHORT).show();

            }
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){

            }
            else {
                Toast.makeText(this, "Denied " + permissions[1], Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this, "Denied " + permissions[0] +" "+permissions[1], Toast.LENGTH_SHORT).show();

        }

    }

    private void initRecyclerView() {
        if (mSongList.size() > 0) {
            songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            final SongListAdapter songListAdapter = new SongListAdapter(this,mSongList, audioList, getSupportFragmentManager(), new AdapterClickInterface() {
                @Override
                public void onAdapterClick(String hrm, String position) {
                    int index = Integer.parseInt(position);
                    Intent intent = new Intent(SongListActivity.this,CurrentSongActivity.class);
                    intent.putExtra("AlbumName",albumName);
                    intent.putExtra("Image",imagepath);
                    Audio audio = audioList.get(index);
                    intent.putExtra("Artist",audio.getArtist());
                    intent.putExtra("SongName",audio.getTitle());
                    startActivity(intent);
                    playAudio(index);
                }
            });

            songListView.setAdapter(songListAdapter);
           /* songListView.addOnItemTouchListener(new_releases RecyclerItemTouchListener(this, new_releases onItemClickListener() {
                @Override
                public void onClick(View view, int index) {
                    playAudio(index);
                }
            }));*/

        }
    }


    private void playAudio(int audioIndex) {
        //Check is service is active
        if (!audioserviceBound) {
            //Store Serializable audioList to SharedPreferences
            StorageUtil storage = new StorageUtil(getApplicationContext());
            storage.storeAudio(audioList);
            storage.storeAudioIndex(audioIndex);

            audioplayerIntent = new Intent(this, MediaPlayerService.class);
            audioplayerIntent.putExtra("AlbumImage",imagepath);
            startService(audioplayerIntent);
            bindService(audioplayerIntent, audioserviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            //Store the new_releases audioIndex to SharedPreferences
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
            songNames = new ArrayList<>();
            while (cursor.moveToNext()) {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));

                // Save to audioList

                songNames.add(title);


                audioList.add(new Audio(data, title, album, artist,imagepath));

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
                String image = (String) documentSnapshot.get("AlbumCover");
                mAlbumSongMap = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("SongList");
                Log.i("SONGLIST ",albumName+" "+String.valueOf(mAlbumSongMap));
                if (mAlbumSongMap != null ) {

                    mPopulateSongList(image);
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

    private void mPopulateSongList(String image) {
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
                    artist = (String) childPair.getValue();
                    list.add(new SongListClass(parentPair.getKey(), (String) childPair.getValue(),imagepath,""));
                   // artistName = childPair.getValue().toString();

                }
                if(childPair.getKey().equals("SongUrl")){

                    songurl = (String) childPair.getValue();
                    //list.add(new_releases SongListClass(parentPair.getKey(), artistName,imagepath,songurl));
                    songNames.add(songName);

                    audioList.add(new Audio(songurl, songName, albumName, artist,image));
                    Log.i("SONGLIST", ""+songName+" : "+songurl);


                }
            }



        }

        songListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        SongListAdapter songListAdapter = new SongListAdapter(this, list, audioList,getSupportFragmentManager(), new AdapterClickInterface() {
            @Override
            public void onAdapterClick(String hrm, String position) {
                int index = Integer.parseInt(position);
                Intent intent = new Intent(SongListActivity.this,CurrentSongActivity.class);
                intent.putExtra("AlbumName",albumName);
                intent.putExtra("Image",imagepath);
                Audio audio = audioList.get(index);
                intent.putExtra("Artist",audio.getArtist());
                intent.putExtra("SongName",audio.getTitle());
                startActivity(intent);
                playAudio(index);
            }
        });

        songListView.setAdapter(songListAdapter);
        /*songListView.addOnItemTouchListener(new_releases RecyclerItemTouchListener(this, new_releases onItemClickListener() {
            @Override
            public void onClick(View view, int index) {
                bufferAudio(index);
            }
        }));*/


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onBottomOptionSelected(String songurl, String songname) {
        songNameDownload = songname;
        songUrlDownload = songurl;
        checkStoreagePersmission();

     //   saveToDownloadDirectory(songurl,songname);
      // Toast.makeText(SongListActivity.this,songurl+songurl.length(),Toast.LENGTH_SHORT).show();
       // Toast.makeText(SongListActivity.this,songname,Toast.LENGTH_SHORT).show();

    }
}
