package com.logix.symphony;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.logix.symphony.Adapters.BrowseRecyclerAdapter;
import com.logix.symphony.Model.BrowseDataModel;

import java.util.ArrayList;


public class BrowseFragment extends Fragment {

    RecyclerView recyclerView;
    BrowseRecyclerAdapter browseRecyclerAdapter;
    ArrayList<BrowseDataModel> list;


    private OnFragmentInteractionListener mListener;

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

        recyclerView = view.findViewById(R.id.albumSongList);

        list = new ArrayList<>();
        for(int i=1;i<=6;i++){
            list.add(new BrowseDataModel(R.drawable.normani,"Sam Smith"));
            list.add(new BrowseDataModel(R.drawable.zara,"So Good"));

        }

        browseRecyclerAdapter = new BrowseRecyclerAdapter(getActivity(),list);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(browseRecyclerAdapter);
      //  setData();


        return view;
    }

    private void setData() {
        for(int i=1;i<6;i++){
            list.add(new BrowseDataModel(R.drawable.normani,"Sam Smith"));
            list.add(new BrowseDataModel(R.drawable.zara,"So Good"));

        }
        browseRecyclerAdapter.notifyDataSetChanged();
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
