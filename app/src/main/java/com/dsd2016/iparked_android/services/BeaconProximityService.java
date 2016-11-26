package com.dsd2016.iparked_android.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.dsd2016.iparked_android.myClasses.Beacon;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;


public class BeaconProximityService extends Service implements BeaconConsumer, RangeNotifier {

    private ArrayList<Beacon> beaconList;
    private BeaconManager beaconManager;

    public BeaconProximityService() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        /** Instantiate collections */
        beaconList = new ArrayList<>();

        /** Register intents */
        registerReceiver(getBeacons, new IntentFilter("com.dsd2016.iparked_android.get_beacons"));

        /** Register beacon monitoring */
        beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        /** Remove beacon scanning schedule */
        unregisterReceiver(getBeacons);
        beaconManager.unbind(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(this);

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("iParkedId", null, null, null));
        } catch (RemoteException e) {
            Log.v("iParked", "Remote exception");
        }

    }


    @Override
    public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> collection, Region region) {
        beaconList.clear();
        for (org.altbeacon.beacon.Beacon beacon : collection) {

            String uuid = beacon.getId1().toString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            String name = beacon.getBluetoothName();
            double distance = beacon.getDistance();

            Beacon visible = new Beacon(major,minor,beacon.getTxPower(),beacon.getRssi(),name,uuid);
            beaconList.add(visible);

        }
    }


    public void returnNearbyBeacons() {

        /** Broadcasts intent containing the list of nearby beacons */
        Intent test=new Intent("com.dsd2016.iparked_android.return_beacons");
        test.putParcelableArrayListExtra("BeaconList",beaconList);
        sendBroadcast(test);


    }


    /** Broadcast receiver for getting and returning the list of beacons */
    private final BroadcastReceiver getBeacons = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.dsd2016.iparked_android.get_beacons")) {
                returnNearbyBeacons();
            }
        }

    };

}
