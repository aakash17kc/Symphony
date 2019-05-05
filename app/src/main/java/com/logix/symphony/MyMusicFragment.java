package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyMusicFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    ImageView profileImage;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String mUserName;
    Uri mPhotoUrl;
    TextView mName;
    String mPhotoString;

    Button premiumAccount;

    public MyMusicFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my_music,container,false);
        profileImage = view.findViewById(R.id.user_image);
        mName = view.findViewById(R.id.user_name);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
            view.findViewById(R.id.premium_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PreminumActivity.class));
            }
        });


        if(mUser!=null){
            mUserName = mUser.getDisplayName();
            mPhotoUrl = mUser.getPhotoUrl();
        }


            mName.setText(mUserName);
        Glide.with(getActivity())
                .load(mPhotoUrl)
                .circleCrop()
                .into(profileImage);
        LinearLayout logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getActivity(),"Logged Out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),SignUpActivity.class));
                getActivity().finish();
            }
        });


        return view;

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
