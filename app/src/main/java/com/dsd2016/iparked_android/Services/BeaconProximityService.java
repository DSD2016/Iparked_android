package com.dsd2016.iparked_android.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.dsd2016.iparked_android.MyClasses.Beacon;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class BeaconProximityService extends Service implements BeaconConsumer, RangeNotifier {

    private List<Beacon> beaconList;
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
        registerReceiver(getBeacons, new IntentFilter("com.dsd2016.iparked_android.get_service_beacons"));

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
        } catch (RemoteException e) {}

    }


    @Override
    public void didRangeBeaconsInRegion(Collection<org.altbeacon.beacon.Beacon> collection, Region region) {
        for (org.altbeacon.beacon.Beacon beacon : collection) {

            String uuid = beacon.getId1().toString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            double distance = beacon.getDistance();

            Beacon visible = new Beacon(uuid, major, minor, distance);
            beaconList.add(visible);
        }
    }


    public void returnNearbyBeacons() {
        /** Broadcasts intent containing the list of nearby beacons */
        Log.v("iParked", "Return beacons");
    }


    /** Broadcast receiver for getting and returning the list of beacons */
    private final BroadcastReceiver getBeacons = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.dsd2016.iparked_android.get_service_beacons")) {
                returnNearbyBeacons();
            }
        }

    };

}
