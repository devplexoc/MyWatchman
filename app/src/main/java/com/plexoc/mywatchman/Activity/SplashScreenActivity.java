package com.plexoc.mywatchman.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.plexoc.mywatchman.BuildConfig;
import com.plexoc.mywatchman.R;
import com.plexoc.mywatchman.Utils.Constants;
import com.plexoc.mywatchman.Utils.Prefs;

public class SplashScreenActivity extends AppCompatActivity {


    private FirebaseRemoteConfig firebaseRemoteConfig;
    private String AppVersion;
    private String Priority;
    private ConstraintLayout btn_skip, btn_update;
    private final String PACKAGE_NAME = "com.plexoc.mywatchman";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful())
                return;
            Log.e("token",task.getResult().getToken());
            Constants.DeviceToken = task.getResult().getToken();
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setMinimumFetchIntervalInSeconds(60)
                        .build();
                firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

                firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(SplashScreenActivity.this, task -> {

                });

                Priority = firebaseRemoteConfig.getString("Priority");
                AppVersion = firebaseRemoteConfig.getString("App_Version");

                Log.e("AppVersion", AppVersion);
                Log.e("AppPriority", Priority);

                if (AppVersion != null) {
                    if (!AppVersion.isEmpty()) {
                        if (Integer.parseInt(AppVersion) > BuildConfig.VERSION_CODE) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
                            builder.setView(R.layout.update_dialog);
                            builder.setCancelable(false);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            btn_skip = alertDialog.findViewById(R.id.constraintLayout_button_cancel);
                            btn_update = alertDialog.findViewById(R.id.constraintLayout_button_save);

                            if (Priority != null) {
                                if (Priority.equalsIgnoreCase("2"))
                                    btn_skip.setVisibility(View.GONE);
                                else
                                    btn_skip.setVisibility(View.VISIBLE);
                            }

                            btn_skip.setOnClickListener(view -> {
                                alertDialog.dismiss();
                                openApp();
                            });
                            btn_update.setOnClickListener(view -> {
                                alertDialog.dismiss();
                                Uri marketUri = Uri.parse("market://details?id=" + PACKAGE_NAME);
                                Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                                startActivity(marketIntent);
                                finish();
                            });

                        } else
                            openApp();
                    } else
                        openApp();
                } else
                    openApp();

            }
        }, 3000);
    }

    private void openApp() {

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
}
