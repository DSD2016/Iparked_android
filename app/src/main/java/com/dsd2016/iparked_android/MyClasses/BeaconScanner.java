package com.dsd2016.iparked_android.MyClasses;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Mihovil and Hrvoje on 14.11.2016..
 */

public class BeaconScanner {

    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 10000;
    private List<ScanFilter> filters;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private Handler mHandler;
    private boolean scanning;
    private List<Beacon> beaconList;

    public List<Beacon> getBeaconList() {
        return beaconList;
    }

    public BeaconScanner(Context context) {
        mHandler = new Handler();
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            ((Activity)context).finish();
        }
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        beaconList = new ArrayList<Beacon>();
    }

    public void scanForBeacons (){

        if (Build.VERSION.SDK_INT > 20) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setReportDelay(0)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();
            filters = new ArrayList<ScanFilter>();
        }
        scanLeDevice();
    }

    public boolean isBluetoothEnabled(){
        if ( mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return true;
        }else{
            return false;
        }
    }
    public boolean isScanning(){
        if ( mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            return scanning;
        }else{
            return false;
        }
    }
    public void stopScanForBeacons() {
        scanning = false;
        if (Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } else {
            mLEScanner.stopScan(mScanCallback);
        }
    }

    private void scanLeDevice() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanForBeacons();
            }
        }, SCAN_PERIOD);

        scanning = true;
        if (Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mLEScanner.startScan(null, settings, mScanCallback);
        }
    }

    @SuppressLint("NewApi")
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            //Log.i("bah", "result "+result.toString());
            ScanRecord record = result.getScanRecord();
            SparseArray<byte[]> data = record.getManufacturerSpecificData();
            int manKey = data.keyAt(0);
            int major = ((data.get(manKey)[18] << 8) | (data.get(manKey)[19] & 0xFF)) & 0xFFFF;
            int minor = ((data.get(manKey)[20] << 8) | (data.get(manKey)[21] & 0xFF)) & 0xFFFF;
            int RSSI = result.getRssi();
            String deviceName = record.getDeviceName();
            String UUID = "";
            try{
                UUID = record.getServiceUuids().toString();
            }
            catch (NullPointerException e){
                UUID = "--";
            }
            Log.i("bah", "Device name: "+deviceName+"  UUID: "+UUID+"  major/minor: "+major+"/"+minor+"   RSSI: "+RSSI);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("bah", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Error", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] record) {
                    int major = ((record[18] << 8) | (record[19] & 0xFF)) & 0xFFFF;
                    int minor = ((record[20] << 8) | (record[21] & 0xFF)) & 0xFFFF;
                    int RSSI = rssi;
                    String deviceName = device.getName();
                    String UUID = "";
                    try{
                        StringBuilder buffer = new StringBuilder();
                        for(int i = 2; i < 18; i++) {
                            buffer.append(String.format("%02x", record[i]));
                        }
                        UUID = buffer.toString();
                    }
                    catch (NullPointerException e){
                        UUID = "--";
                    }
                    Log.i("bah", "Device name: "+deviceName+"  UUID: "+UUID+"  major/minor: "+major+"/"+minor+"   RSSI: "+RSSI);
                }
            };
}
