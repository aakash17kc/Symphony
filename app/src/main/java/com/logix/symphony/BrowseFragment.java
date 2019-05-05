package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.logix.symphony.Adapters.BrowseRecyclerAdapter;
import com.logix.symphony.Model.BrowseDataModel;

import java.util.ArrayList;
import java.util.HashMap;


public class BrowseFragment extends Fragment {

    RecyclerView recyclerView;
    BrowseRecyclerAdapter browseRecyclerAdapter;
    ArrayList<String> albumList = new ArrayList<>();

    DocumentReference documentReference;
    FirebaseFirestore firebaseFirestore;

    ArrayList<BrowseDataModel> browseDataModelArrayList = new ArrayList<>();



    private OnFragmentInteractionListener mListener;
    private ArrayList<String> mSongList;
    private HashMap<String,String> mSongMap = new HashMap<>();
    int counter=0;

    public BrowseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_browse,container,false);

        //recyclerView = view.findViewById(R.id.albumSongMap);

        firebaseFirestore = FirebaseFirestore.getInstance();

       CardView onDevice = view.findViewById(R.id.onDevice);

        onDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), DeviceSongAlbumsActivity.class));
            }
        });

       /* browseRecyclerAdapter = new BrowseRecyclerAdapter(getActivity(), browseDataModelArrayList);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(browseRecyclerAdapter);
        mGetData();*/




        return view;
    }

    private void mGetData() {


        firebaseFirestore.collection("AllSongs").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    albumList = new ArrayList<>();

                    mSongList = new ArrayList<>();
                    int i = 0;

                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        mSongList.add(String.valueOf(queryDocumentSnapshot.getId()));
                      //  mSongList.add(queryDocumentSnapshot.getId());
                    }


                    mSetData(mSongList);
                    Log.i("LIST", String.valueOf(mSongList.size()));


                } else {
                    Toast.makeText(getActivity(), "  " + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }


        });



    }

    private void mSetData(ArrayList<String> mSongList) {

        for(int i = 0; i< mSongList.size(); i++){
            mSongMap = new HashMap<>();
            documentReference = firebaseFirestore.document("AllSongs/"+ mSongList.get(i));

            Log.i("BROWSE LIST", " " + mSongList.get(i));

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.i("SUCCESS","DATA ADDED" +documentSnapshot.get("Artist"));

                    String albumCover = (String) documentSnapshot.get("AlbumCover");
                //    Log.i("ALBUMCOVER", albumCover);

                    albumList.add(albumCover);
                    browseDataModelArrayList.add(new BrowseDataModel(albumCover, (String) documentSnapshot.get("Artist")));
                    browseRecyclerAdapter.notifyDataSetChanged();

                    Log.i("LIST", String.valueOf(albumList));

                }

            });
           // browseRecyclerAdapter.notifyDataSetChanged();




        }





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
