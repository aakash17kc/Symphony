package com.logix.symphony;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SongActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, SearchFragment.OnFragmentInteractionListener,
        BrowseFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, MyMusicFragment.OnFragmentInteractionListener {

    FirebaseAuth mAuth;

    String mUserName,mUserEmail;
    Uri mUserPhotoUrl;

    ImageView mUserImage;
    TextView mShowName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new HomeFragment());

        TextView curPlaySongName = findViewById(R.id.current_song_name);
        curPlaySongName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SongActivity.this,CurrentSongActivity.class));
            }
        });

        }

    public Boolean mLaunchFragment(Fragment fragment){

        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.search_music:
                fragment = new SearchFragment();
                break;
            case R.id.browse_music:
                fragment = new BrowseFragment();
                break;
            case R.id.my_music:
                fragment = new MyMusicFragment();
                break;

        }
        return mLaunchFragment(fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
