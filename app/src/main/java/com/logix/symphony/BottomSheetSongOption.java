package com.logix.symphony;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.logix.symphony.R;

public class BottomSheetSongOption extends BottomSheetDialogFragment {

    BottomSheetListener bottomSheetListener;
    Bundle bundle;
    String songurl="",songname="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

        Button download = view.findViewById(R.id.downloadSong);
        Button playlist = view.findViewById(R.id.add_to_playlist);

        bundle = getArguments();
        if(bundle!=null){
            songurl = bundle.getString("SongUrl");
            songname = bundle.getString("SongName");
        }



        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetListener.onBottomOptionSelected(songurl,songname);
                dismiss();
            }
        });

        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetListener.onBottomOptionSelected(songurl,songname);
                dismiss();


            }
        });

        return view;
    }

    public interface BottomSheetListener{
       void onBottomOptionSelected(String songurl, String songname);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            bottomSheetListener = (BottomSheetListener) context;
        }
        catch (Exception e){
            throw new ClassCastException(context.toString()+"implement Listener");
        }
    }
}
