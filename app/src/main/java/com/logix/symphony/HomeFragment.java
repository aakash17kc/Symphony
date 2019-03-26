package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    String mUserName,mUserEmail;
    Uri mUserPhotoUrl;

    ImageView mUserImage;
    TextView mShowName;
    FirebaseAuth mAuth;
    TextView mPlayListName;
    RecyclerView mTrendingRecycler;
    ArrayList<SongClass> list = new ArrayList<>();

    private OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();


        View songLayout = LayoutInflater.from(getActivity()).inflate(R.layout.song_list_home,null);
        mTrendingRecycler = songLayout.findViewById(R.id.recyclerTrending);
        mPlayListName = songLayout.findViewById(R.id.playlistName);

        for (int i=0;i<10;i++){
            list.add(new SongClass("So Good",R.drawable.zara));
        }

        mTrendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

        SongAdapter songAdapter = new SongAdapter(getActivity(),list);
        mTrendingRecycler.setAdapter(songAdapter);
        mPlayListName.setText("Trending");

        LinearLayout layout = view.findViewById(R.id.songListInflate);
        layout.addView(songLayout);
        list.clear();
        songLayout = LayoutInflater.from(getActivity()).inflate(R.layout.song_list_home,null);

        mTrendingRecycler = songLayout.findViewById(R.id.recyclerTrending);
        mPlayListName = songLayout.findViewById(R.id.playlistName);

        for (int i=0;i<10;i++){
            list.add(new SongClass("So Good",R.drawable.normani));
        }

        mTrendingRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));

         songAdapter = new SongAdapter(getActivity(),list);
        mTrendingRecycler.setAdapter(songAdapter);
        mPlayListName.setText("Trending");

        layout.addView(songLayout);

        mTrendingRecycler.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),mTrendingRecycler,new RecyclerItemClickListener.ClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getActivity(),SongListActivity.class));

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));








        FirebaseUser user = mAuth.getCurrentUser();



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
