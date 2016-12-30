package com.dsd2016.iparked_android.services;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.dsd2016.iparked_android.myClasses.Beacon;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.JsonBeacon;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.service.ArmaRssiFilter;
import org.altbeacon.beacon.service.RunningAverageRssiFilter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

import static java.lang.Math.abs;


public class BeaconProximityService extends Service implements BeaconConsumer, RangeNotifier, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ArrayList<Beacon> beaconList = new ArrayList<>();
    private ArrayList<Beacon> visiblePersonalBeacons = new ArrayList<>();
    private ArrayList<JsonBeacon> visibleGarageBeacons = new ArrayList<>();
    private BeaconManager beaconManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double maxDistance = 1.5;

    public BeaconProximityService() {
        super();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        /** Instantiate collections */
        beaconList = new ArrayList<>();

        /** Register intents */
        registerReceiver(getBeaconsOrLocation, new IntentFilter("com.dsd2016.iparked_android.get_beacons"));
        registerReceiver(getBeaconsOrLocation, new IntentFilter("com.dsd2016.iparked_android.get_location"));

        /** Register beacon monitoring */
        beaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.setBackgroundBetweenScanPeriod(1000l);
        beaconManager.setBackgroundScanPeriod(1000l);
        beaconManager.setForegroundBetweenScanPeriod(1000l);
        beaconManager.setBackgroundScanPeriod(1000l);
        beaconManager.setRssiFilterImplClass(ArmaRssiFilter.class);
        ArmaRssiFilter.setDEFAULT_ARMA_SPEED(0.5);
        beaconManager.bind(this);

        /** Preparing the Google Api to get the location */
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mLastLocation = new Location("");
        mLastLocation.setLongitude(0.0);
        mLastLocation.setLatitude(0.0);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        /** Remove beacon scanning schedule */
        unregisterReceiver(getBeaconsOrLocation);
        beaconManager.unbind(this);
        mGoogleApiClient.disconnect();
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
        visibleGarageBeacons.clear();
        beaconList.clear();
        visiblePersonalBeacons.clear();
        ArrayList<Beacon> personalBeaconList = IparkedApp.mDbHelper.getPersonalBeacons();
        Double sumLong=0.0;
        Double sumLat=0.0;
        for (org.altbeacon.beacon.Beacon visiblebeacon : collection){
            for(JsonBeacon b: ((IparkedApp) getApplication()).getJsonBeacon()){
                if(visiblebeacon.getBluetoothAddress().equals(b.getBluetooth_address())){
                    visibleGarageBeacons.add(b);
                }
            }
        }
        for (org.altbeacon.beacon.Beacon beacon : collection) {

            /** Get beacon information */
            String uuid = beacon.getId1().toString();
            int major = beacon.getId2().toInt();
            int minor = beacon.getId3().toInt();
            String name = beacon.getBluetoothName();
            double distance = beacon.getDistance();
            String address = beacon.getBluetoothAddress();
            Beacon visible = new Beacon(major, minor, name, uuid, distance, address);

            beaconList.add(visible);

            /** You need to have at least one personal beacon */
            if (personalBeaconList == null) {
                continue;
            }


            /** Check if beacon is personal beacon */
            for (Beacon personalBeacon : personalBeaconList) {

                if (visible.getAddress().equals(personalBeacon.getAddress())) {

                    /** If beacon is closer than the defined, set it's location to null */
                    if (visible.getDistance() < maxDistance) {
                        visiblePersonalBeacons.add(visible);
                        if ( !isLocationNull(personalBeacon.getLocation()) ) {
                            IparkedApp.mDbHelper.updateBeaconLocation(visible);
                        }
                    }

                    /** If personal beacon is visible but not nearby we need to set its location */
                    else {

                        if ( isLocationNull(personalBeacon.getLocation()) ) {
                            personalBeacon.setLocation(getLocation());
                            IparkedApp.mDbHelper.updateBeaconLocation(personalBeacon);
                            if(!visibleGarageBeacons.isEmpty()){
                                ((IparkedApp) getApplication()).getLocationInGarage().setFloor_id(visibleGarageBeacons.get(0).getFloor_id());
                                for (JsonBeacon b : visibleGarageBeacons){
                                    sumLong+=b.getLongitude();
                                    sumLat+=b.getLatitude();
                                }
                                ((IparkedApp) getApplication()).getLocationInGarage().setLongitude(sumLong/visibleGarageBeacons.size());
                                ((IparkedApp) getApplication()).getLocationInGarage().setLatitude(sumLat/visibleGarageBeacons.size());
                            }
                            else{
                                ((IparkedApp) getApplication()).setLocationInGarage(null);
                            }
                        }
                    }
                }
            }
        }

        /** Don't do anything if lists are still not initialized */
        if (personalBeaconList == null || visiblePersonalBeacons == null) {
            returnLocation();
            returnNearbyBeacons();
            return;
        }

        /** Search if personal beacon stopped being visible */
        for (Beacon personalBeacon : personalBeaconList) {
            boolean found = false;

            for (Beacon visiblePersonalBeacon : visiblePersonalBeacons) {
                if (personalBeacon.getAddress().equals(visiblePersonalBeacon.getAddress())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                if ( isLocationNull(personalBeacon.getLocation()) ) {
                    personalBeacon.setLocation(getLocation());
                    IparkedApp.mDbHelper.updateBeaconLocation(personalBeacon);
                }
            }
        }
        returnLocation();
        returnNearbyBeacons();
    }

    /** Helper function that checks if location is null */
    private boolean isLocationNull(Location location) {
        return abs(location.getLatitude()) <= 0.01 && abs(location.getLongitude()) <= 0.01;
    }

    /** Checks and returns beacon location */
    private Location getLocation() {

        /** Check for permission */
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        mGoogleApiClient.connect();
        do {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } while (mLastLocation == null);
        mGoogleApiClient.disconnect();

        return mLastLocation;
    }

    public void returnNearbyBeacons() {

        /** Broadcasts intent containing the list of nearby beacons */
        Intent beaconListIntent = new Intent("com.dsd2016.iparked_android.return_beacons");
        beaconListIntent.putParcelableArrayListExtra("beaconList", beaconList);
        sendBroadcast(beaconListIntent);


    }

    private void returnLocation() {

        /** Broadcasts intent containing the the last location */
        Intent lastLocationIntent = new Intent("com.dsd2016.iparked_android.return_location");
        lastLocationIntent.putExtra("latitude", mLastLocation.getLatitude());
        lastLocationIntent.putExtra("longitude", mLastLocation.getLongitude());
        sendBroadcast(lastLocationIntent);
    }


    /** Broadcast receiver for getting and returning the list of beacons Or the last Location */
    private final BroadcastReceiver getBeaconsOrLocation = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.dsd2016.iparked_android.get_beacons")) {
                returnNearbyBeacons();
            } else if (action.equals("com.dsd2016.iparked_android.get_location")) {
                mGoogleApiClient.connect();
            }
        }

    };


    /** Methods to connect and retrieve user last location */

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            returnLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
