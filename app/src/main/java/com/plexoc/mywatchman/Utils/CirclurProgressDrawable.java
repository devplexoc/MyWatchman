package com.plexoc.mywatchman.Utils;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class CirclurProgressDrawable {


    public static CircularProgressDrawable ShowProgressDrwable(Context context){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
}
