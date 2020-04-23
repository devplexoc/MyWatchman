package com.plexoc.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.plexoc.myapplication.Fragment.DashboardFragment;
import com.plexoc.myapplication.R;
import com.plexoc.myapplication.Utils.Prefs;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Prefs.getString(Prefs.USER).isEmpty()) {
                    Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginSignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

}
