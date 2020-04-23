package com.plexoc.myapplication.Utils;

import android.app.Application;
import android.content.ContextWrapper;


public class App extends Application {

    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
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
