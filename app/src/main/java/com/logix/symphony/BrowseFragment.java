package com.logix.symphony;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
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
    private boolean flag = false;

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
                checkStoreagePersmission();
                if(flag)
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

    public  void checkStoreagePersmission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
                flag = true;
                startActivity(new Intent(getActivity(), DeviceSongAlbumsActivity.class));


            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                flag = true;
                startActivity(new Intent(getActivity(), DeviceSongAlbumsActivity.class));

            }
            else {
                Toast.makeText(getActivity(), "Denied " + permissions[0], Toast.LENGTH_SHORT).show();

            }
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){

            }
            else {
                Toast.makeText(getActivity(), "Denied " + permissions[1], Toast.LENGTH_SHORT).show();

            }

        } else {
            Toast.makeText(getActivity(), "Denied " + permissions[0] +" "+permissions[1], Toast.LENGTH_SHORT).show();

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
