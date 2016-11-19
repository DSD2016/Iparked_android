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

import com.dsd2016.iparked_android.MyClasses.Beacon;
import com.dsd2016.iparked_android.MyClasses.BeaconScanner;
import com.dsd2016.iparked_android.MyClasses.ParcelableBeaconList;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class BeaconProximityService extends Service {

    // TODO substitute with value from UI
    private int interval = 1500;
    private BeaconScanner beaconScanner;
    private List<Beacon> beaconList;
    private Handler mHandler = new Handler();

    public BeaconProximityService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(this.broadCastNewMessage, new IntentFilter("gimmeSomeBeacons"));
        beaconScanner = new BeaconScanner();
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
        beaconScanner.scanForBeacons(1000);
        try {
            sleep(1000);
        }catch (InterruptedException e){
            Log.v("bah", "Woke up early!");
        }
        beaconList = beaconScanner.getBeaconList();

    }

    BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent i = new Intent();
            i.setAction("HereAreSomeBeacons");
            i.putExtra("BeaconList", new ParcelableBeaconList(beaconList));
            sendBroadcast(i);
            //Log.i("bah",intent.getAction());

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
