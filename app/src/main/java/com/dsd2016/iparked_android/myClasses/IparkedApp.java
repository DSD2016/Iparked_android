package com.dsd2016.iparked_android.myClasses;

import android.app.Application;


public class IparkedApp extends Application {

    public static BeaconDbHelper mDbHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        mDbHelper=new BeaconDbHelper(getApplicationContext());
    }

}
