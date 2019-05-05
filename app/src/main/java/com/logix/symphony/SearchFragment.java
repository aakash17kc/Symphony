package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.logix.symphony.Adapters.BrowseRecyclerAdapter;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.BrowseDataModel;
import com.logix.symphony.Model.SearchResultModel;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    BrowseRecyclerAdapter browseRecyclerAdapter;
    ArrayList<String> albumList = new ArrayList<>();

    DocumentReference documentReference;
    FirebaseFirestore firebaseFirestore;
     EditText serach;
    String querytext;

    ArrayList<BrowseDataModel> browseDataModelArrayList = new ArrayList<>();
    ArrayList<SearchResultModel> searchResultModelArrayList = new ArrayList<>();


    private ArrayList<String> mSongList;
    private HashMap<String,String> mSongMap = new HashMap<>();
    int counter=0;
     AppBarLayout appBarLayout;
    FirestoreRecyclerAdapter firestoreRecyclerAdapter;
    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_search,container,false);

        recyclerView = view.findViewById(R.id.albumSongList);

        firebaseFirestore = FirebaseFirestore.getInstance();
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        serach = view.findViewById(R.id.search_bar);
        RelativeLayout relativeLayout = view.findViewById(R.id.search_relative);
        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false);
            }
        });





        browseRecyclerAdapter = new BrowseRecyclerAdapter(getActivity(), searchResultModelArrayList, new AdapterClickInterface() {
            @Override
            public void onAdapterClick(String hrm, String image) {
                Intent intent = new Intent(getActivity(),SongListActivity.class);
                intent.putExtra("AlbumName",hrm);
                intent.putExtra("Image",image);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(browseRecyclerAdapter);
        mGetData();

        view.findViewById(R.id.buttonsearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = serach.getText().toString().trim();

                if(query.length()!=0){
                    searchResults(query);
                }
            }
        });
      /*  serach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {




            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });*/
     // searchResults("");

        return view;
    }


    private void searchResults(String searchText){
        querytext="";

       querytext = Character.toUpperCase(searchText.charAt(0))+searchText.substring(1);
      Toast.makeText(getActivity(),querytext,Toast.LENGTH_SHORT).show();

        documentReference = firebaseFirestore.document("AllSongs/"+querytext);
        searchResultModelArrayList = new ArrayList<>();
        browseRecyclerAdapter = new BrowseRecyclerAdapter(getActivity(), searchResultModelArrayList, new AdapterClickInterface() {
            @Override
            public void onAdapterClick(String hrm, String image) {
                Intent intent = new Intent(getActivity(),SongListActivity.class);
                intent.putExtra("AlbumName",hrm);
                intent.putExtra("Image",image);
                startActivity(intent);
            }
        });
        albumList = new ArrayList<>();


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.i("SUCCESS","DATA ADDED" +documentSnapshot.get("Artist"));

                String albumCover = (String) documentSnapshot.get("AlbumCover");
                String artist = (String) documentSnapshot.get("Artist");
                //    Log.i("ALBUMCOVER", albumCover);

                albumList.add(albumCover);
              //  browseDataModelArrayList.add(new BrowseDataModel(albumCover, (String) documentSnapshot.get("Artist")));
                searchResultModelArrayList.add(new SearchResultModel(querytext,artist,albumCover));
                browseRecyclerAdapter.notifyDataSetChanged();

                Log.i("LIST", String.valueOf(albumList));

            }

        });

        recyclerView.setAdapter(browseRecyclerAdapter);





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



    private void mSetData(final ArrayList<String> mSongList) {

        for(int i = 0; i< mSongList.size(); i++){
            mSongMap = new HashMap<>();
            documentReference = firebaseFirestore.document("AllSongs/"+ mSongList.get(i));

            Log.i("BROWSE LIST", " " + mSongList.get(i));

            final int finalI = i;
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.i("SUCCESS","DATA ADDED" +documentSnapshot.get("Artist"));

                    String albumCover = (String) documentSnapshot.get("AlbumCover");
                    String artist = (String) documentSnapshot.get("Artist");
                    //    Log.i("ALBUMCOVER", albumCover);

                    albumList.add(albumCover);
                 //   browseDataModelArrayList.add(new BrowseDataModel(albumCover, (String) documentSnapshot.get("Artist")));
                    searchResultModelArrayList.add(new SearchResultModel(mSongList.get(finalI),artist,albumCover));
                    browseRecyclerAdapter.notifyDataSetChanged();

                    Log.i("LIST", String.valueOf(albumList));

                }

            });
            // browseRecyclerAdapter.notifyDataSetChanged();




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
