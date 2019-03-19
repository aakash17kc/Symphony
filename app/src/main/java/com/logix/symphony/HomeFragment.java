package com.logix.symphony;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {


    String mUserName,mUserEmail;
    Uri mUserPhotoUrl;

    ImageView mUserImage;
    TextView mShowName;
    FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        mUserImage = view.findViewById(R.id.profileimage);
        mShowName = view.findViewById(R.id.user_name);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            mUserName = "Hi, "+user.getDisplayName();
            mUserEmail = user.getEmail();
            mUserPhotoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();

            String uid = user.getUid();

            Glide.with(getActivity())
                    .load(mUserPhotoUrl)
                    .centerCrop()
                    .circleCrop()
                    .into(mUserImage);

            mShowName.setText(mUserName);

        }
        else {
            startActivity(new Intent(getContext(),SignUpActivity.class));
        }
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
