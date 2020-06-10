package com.plexoc.mywatchman.Utils;

import android.app.Application;
import android.content.ContextWrapper;

import com.google.firebase.FirebaseApp;


public class App extends Application {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        FirebaseApp.initializeApp(this);
       /* AndroidThreeTen.init(this);*/

        //ProcessLifecycleOwner.get().getLifecycle().addObserver(this);*/

        //SessionManager session = new SessionManager(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }



}
