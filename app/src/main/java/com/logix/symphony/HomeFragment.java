package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.logix.symphony.Adapters.VerticalRecyclerAdapter;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.Model.VerticalRecyclerModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class HomeFragment extends Fragment {


    String mUserName, mUserEmail;
    Uri mUserPhotoUrl;

    ImageView mUserImage;
    TextView mShowName;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;


    TextView mPlayListName;
    RecyclerView verticalRecycler;
    ArrayList<VerticalRecyclerModel> verticalList = new ArrayList<>();
    VerticalRecyclerAdapter verticalRecyclerAdapter;

    ArrayList<String> mSongCat = new ArrayList<>();


    HashMap<String, Object> mCategoryMap = new HashMap<>();
    HashMap<String, HashMap<String, String>> temphashmap = new HashMap<>();

    HashMap<String,HashMap<String,HashMap<String,String>>>  jdbc = new HashMap<>();


    DocumentReference documentReference;


    private OnFragmentInteractionListener mListener;
    private String mAlbumArtist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        progressBar = view.findViewById(R.id.progress_bar);


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        verticalRecycler = view.findViewById(R.id.verticalRecycler);

        verticalRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        verticalRecycler.setHasFixedSize(true);

        HomeFragment homeFragment = new HomeFragment();

        verticalRecyclerAdapter = new VerticalRecyclerAdapter(getActivity(), verticalList, homeFragment, new AdapterClickInterface() {
            @Override
            public void onAdapterClick(String hrm, String image) {
                Toast.makeText(getActivity(), " " + hrm, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SongListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("AlbumName", hrm);
                bundle.putString("Image", image);
                bundle.putInt("Interaction",00);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        verticalRecycler.setAdapter(verticalRecyclerAdapter);
        mFetchSongCategories();



        FirebaseUser user = mAuth.getCurrentUser();
       // temp();


        return view;
    }

    private void mFetchSongCategories() {
        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mSongCat = new ArrayList<>();
                    int i = 0;
                    Map<String, Object> hashMap;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        //mSongCat.add(String.valueOf(queryDocumentSnapshot.getId()));
                        mSongCat.add(queryDocumentSnapshot.getId());
                        Log.i("DATA ", " categories " + mSongCat.get(i));
                        i++;
                    }

                    mFetchCategoriesDetails();
                } else {
                    Toast.makeText(getActivity(), "  " + task.getException(), Toast.LENGTH_SHORT).show();

                }
            }


        });
    }

    private void mFetchCategoriesDetails() {


        for (int i = 0; i < mSongCat.size(); i++) {
            mCategoryMap = new HashMap<>();
            documentReference = firebaseFirestore.document("Categories/" + mSongCat.get(i));

            final int finalI = i;
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    mCategoryMap = (HashMap<String, Object>) documentSnapshot.getData();
                    Log.i("DATA ", String.valueOf(mCategoryMap));
                    setData(mCategoryMap, mSongCat.get(finalI));

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("on failure ", e.getMessage());

                }
            });
            progressBar.setVisibility(View.GONE);
        }

    }




    private void setData(HashMap<String, Object> playlist, String playlistName) {

        ArrayList<HorizontalRecylerModel> album = new ArrayList<>();

        Iterator hmIterator = playlist.entrySet().iterator();
        Map.Entry mapElement;


        while (hmIterator.hasNext()) {
            mapElement = (Map.Entry) hmIterator.next();
            album.add(new HorizontalRecylerModel((String) mapElement.getKey(), (String) mapElement.getValue()));


        }


        VerticalRecyclerModel vrm = new VerticalRecyclerModel(playlistName, album);
        verticalList.add(vrm);


        verticalRecyclerAdapter.notifyDataSetChanged();


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


   /* private void temp() {

        documentReference = firebaseFirestore.document("AllSongs/Zara Larsson");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                temphashmap = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("SongList");
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();

                Log.i("SUCCESS ", String.valueOf(temphashmap));

                jdbc.put("SongList",temphashmap);

                firebaseFirestore.collection("AllSongs").document("Zara Larsson - So Good").set(jdbc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"written",Toast.LENGTH_SHORT).show();
                        Log.i("SUCCESS WRITTEN ", String.valueOf(temphashmap));


                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("on failure ", e.getMessage());

            }
        });
    }*/



    /*@Override
    public void onStart() {
        super.onStart();
        firebaseFirestore.collection("Categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mSongCat = new ArrayList<>();
                    int i = 0;
                    Map<String, Object> hashMap;
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        //mSongCat.add(String.valueOf(queryDocumentSnapshot.getId()));
                        mSongCat.add(queryDocumentSnapshot.getId());
                        Log.i("DATA ", " categories " + mSongCat.get(i));
                        i++;
                    }

                }
            }
        });

        for (int i = 0; i < mSongCat.size(); i++) {
            mCategoryMap = new HashMap<>();
            documentReference = firebaseFirestore.document("Categories/" + mSongCat.get(i));

            final int finalI = i;
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    setData(mCategoryMap, mSongCat.get(finalI));
                }
            });

        }*/

}
