package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.logix.symphony.Adapters.HorizontalRecyclerAdapter;
import com.logix.symphony.Adapters.VerticalRecyclerAdapter;
import com.logix.symphony.Interfaces.AdapterClickInterface;
import com.logix.symphony.Model.HorizontalRecylerModel;
import com.logix.symphony.Model.VerticalRecyclerModel;

import java.util.ArrayList;


public class HomeFragment extends Fragment {


    String mUserName,mUserEmail;
    Uri mUserPhotoUrl;

    ImageView mUserImage;
    TextView mShowName;
    FirebaseAuth mAuth;
    TextView mPlayListName;
    RecyclerView verticalRecycler;
    ArrayList<VerticalRecyclerModel> verticalList = new ArrayList<>();
    VerticalRecyclerAdapter verticalRecyclerAdapter;

    private OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();

        verticalRecycler = view.findViewById(R.id.verticalRecycler);

        verticalRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        verticalRecycler.setHasFixedSize(true);

        HomeFragment homeFragment = new HomeFragment();

        verticalRecyclerAdapter = new VerticalRecyclerAdapter(getActivity(), verticalList,homeFragment, new AdapterClickInterface() {
            @Override
            public void onAdapterClick(String hrm,int image) {
                Toast.makeText(getActivity()," "+hrm,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),SongListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("AlbumName",hrm);
                bundle.putInt("Image",image);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        verticalRecycler.setAdapter(verticalRecyclerAdapter);
        setData();

        FirebaseUser user = mAuth.getCurrentUser();



        return view;
    }




    private void setData() {

        for (int i=1;i<=3;i++){
            ArrayList<HorizontalRecylerModel> album = new ArrayList<>();

            for(int j=0;j<=3;j++){
                album.add(new HorizontalRecylerModel("So Good",R.drawable.zara));
                album.add(new HorizontalRecylerModel("Sam Smith",R.drawable.normani));

            }

            VerticalRecyclerModel vrm = new VerticalRecyclerModel("Trending",album);
            verticalList.add(vrm);


        }
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
}
