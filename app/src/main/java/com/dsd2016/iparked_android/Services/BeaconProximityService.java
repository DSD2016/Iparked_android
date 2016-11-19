package com.dsd2016.iparked_android.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dsd2016.iparked_android.MyClasses.BeaconScanner;

public class BeaconProximityService extends Service {

    // TODO substitute with value from UI
    private int interval = 1000;
    private Handler mHandler = new Handler();

    public BeaconProximityService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(this.broadCastNewMessage, new IntentFilter("gimmeSomeBeacons"));
        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCastNewMessage);
        stopRepeatingTask();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getNearbyBeacons() {
        Log.v("iParked", "GetBeacons");
    }

    BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendBroadcast(new Intent().setAction("HereAreSomeBeacons"));
            Log.i("bah",intent.getAction());

        }
    };

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
