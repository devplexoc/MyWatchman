package com.plexoc.mywatchman.Fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.plexoc.mywatchman.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashLoginFragment extends BaseFragment {


    public SplashLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_splash_login, container, false);

        VideoView videoView = view.findViewById(R.id.videoview);

        Uri uri = Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.raw.bg_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        view.findViewById(R.id.btn_login).setOnClickListener(v -> replaceFragment(new LoginFragment(),null));
        view.findViewById(R.id.btn_signup).setOnClickListener(v -> replaceFragment(new SignupFragment(),null));


        return view;
    }

}
