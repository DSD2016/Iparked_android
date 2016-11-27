package com.dsd2016.iparked_android.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.services.BeaconProximityService;

import java.util.List;


public class SplashActivity extends AppCompatActivity {

    // Totally random numbers for intents
    private static final int LOCATION_PERMISSION_REQUEST = 6577;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** Populate view with UI data */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideActionBar();
        startProximityService();
        animateSplashScreen();
        checkPermissions();
        allowBluetooth();
    }

    private void hideActionBar() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

    }


    private void animateSplashScreen() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /** Make sure Bluetooth and Location are enabled before transition */
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                while ( !mBluetoothAdapter.isEnabled()
                        || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.v("iParked", "Wait failed");
                    }
                }

                /** Animate to next activity */
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 1000);

    }


    private void startProximityService() {

        int serviceStarted = 0;
        /** Start proximity service if it already isn't active */
        ActivityManager manager = (ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BeaconProximityService.class.getName().equals(service.service.getClassName())) {
                serviceStarted = 1;
                break;
            }
        }
        if(serviceStarted==0){
            this.startService(new Intent(this, BeaconProximityService.class));
        }


    }


    private void checkPermissions() {

        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        /** Ask for Fine Location permission */
        if (locationPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        }

    }


    private void allowBluetooth() {

        /** Check if Bluetooth LE is available on the device */
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.error_ble_unavailable, Toast.LENGTH_SHORT).show();
            this.finish();
        }

        /** Turn on Bluetooth */
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST: {
                if ( !(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) )
                    this.finish();
            }

        }

    }
}
