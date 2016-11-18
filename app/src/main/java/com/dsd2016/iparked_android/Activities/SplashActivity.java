package com.dsd2016.iparked_android.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.Services.BeaconProximityService;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /** Populate view with UI data */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        hideActionBar();
        animateSplashScreen();
        allowBluetooth();
        startProximityService();

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

                /** Make sure Bluetooth is enabled  before transition*/
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                while ( !mBluetoothAdapter.isEnabled() ) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Log.v("iParked", "Wait failed");
                    }
                }


                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 1000);

    }


    private void startProximityService() {

        this.startService(new Intent(this, BeaconProximityService.class));

    }


    private void allowBluetooth() {

        /** Check if Bluetooth LE is available on the device */
        if (!this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.error_ble_unavailable, Toast.LENGTH_SHORT).show();
            this.finish();
        }

        /** Ask for Bluetooth permission */
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()){
            Intent intentBtEnabled = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(intentBtEnabled, REQUEST_ENABLE_BT);
        }

    }
}
