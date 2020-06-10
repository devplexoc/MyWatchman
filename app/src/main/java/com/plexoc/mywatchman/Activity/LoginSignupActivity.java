package com.plexoc.mywatchman.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import com.plexoc.mywatchman.Fragment.PlansFragment;
import com.plexoc.mywatchman.Fragment.SplashLoginFragment;
import com.plexoc.mywatchman.R;

public class LoginSignupActivity extends AppCompatActivity {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);

        addFragment(new SplashLoginFragment());


      /*  videoView = findViewById(R.id.videoview);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });*/

    }

    public void replaceFragment(Fragment fragment, String fragmentTag) {

        if (fragmentTag == null) {
            getSupportFragmentManager().popBackStack(fragment.getClass().getName(), 1);
        } else {
            getSupportFragmentManager().popBackStack(fragmentTag, 1);
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(fragmentTag == null ? fragment.getClass().getName() : fragmentTag);
        transaction.commitAllowingStateLoss();

    }

    public void addFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.container) instanceof PlansFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
