package com.dsd2016.iparked_android.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dsd2016.iparked_android.MyClasses.BeaconScanner;

public class BeaconProximityService extends Service {

    // TODO substitute with value from UI
    private int interval = 1000;
    private Handler mHandler = new Handler();
    private BeaconScanner scanner;

    public BeaconProximityService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startRepeatingTask();
        scanner = new BeaconScanner(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getNearbyBeacons() {
        Log.v("iParked", "GetBeacons");
        scanner.scanForBeacons();
    }

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {
            getNearbyBeacons();
            mHandler.postDelayed(mHandlerTask, interval);
        }
    };

    void startRepeatingTask() {
        mHandlerTask.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask);
    }
}
