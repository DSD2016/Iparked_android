package com.dsd2016.iparked_android.MyClasses;

import android.annotation.SuppressLint;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

import android.os.Build;
import android.os.Handler;
import android.util.Log;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;


public class BeaconScanner {

    private BluetoothAdapter mBluetoothAdapter;
    private List<ScanFilter> filters;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private Handler mHandler;
    private boolean scanning;
    private List<Beacon> beaconList;

    public List<Beacon> getBeaconList() {
        return beaconList;
    }


    public BeaconScanner() {
        mHandler = new Handler();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        beaconList = new ArrayList<>();
    }

    public void scanForBeacons (long scanPeriod) {

        if (Build.VERSION.SDK_INT > 20) {
            mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            settings = new ScanSettings.Builder()
                    .setReportDelay(0)
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                    .build();
            filters = new ArrayList<>();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScanForBeacons();
            }
        }, scanPeriod);

        scanning = true;
        if (Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mLEScanner.startScan(null, settings, mScanCallback);
        }

    }

    public boolean isBluetoothEnabled(){
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }

    public boolean isScanning(){
        return isBluetoothEnabled() && scanning;
    }

    public void stopScanForBeacons() {
        scanning = false;
        if (Build.VERSION.SDK_INT < 21) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } else {
            mLEScanner.stopScan(mScanCallback);
        }
    }


    @SuppressLint("NewApi")
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            ScanRecord scanRecord = result.getScanRecord();
            assert scanRecord != null;
            SparseArray<byte[]> data = scanRecord.getManufacturerSpecificData();
            int manKey = data.keyAt(0);
            byte[] record = data.get(manKey);
            int rssi = result.getRssi();
            String name = scanRecord.getDeviceName();

            addBeaconRecord(record, name, rssi);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("bah", sr.toString());
                // TODO add something useful or remove
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

                    String name = device.getName();
                    addBeaconRecord(record, name, rssi);
                }
            };

    private void addToBeaconList(int major, int minor, int txPower, int rssi, String name, String uuid) {
        for(Beacon b : beaconList) {
            if(b.getMajor() == major && b.getMinor() == minor) {
                beaconList.remove(b);
                break;
            }
        }
        beaconList.add(new Beacon(major, minor, txPower, rssi, name, uuid));
    }

    private void addBeaconRecord(byte[] record, String name, int rssi) {

        int major = ((record[18] << 8) | (record[19] & 0xFF)) & 0xFFFF;
        int minor = ((record[20] << 8) | (record[21] & 0xFF)) & 0xFFFF;
        int txPower = record[22];
        String uuid;

        try {
            StringBuilder buffer = new StringBuilder();
            for(int i = 2; i < 18; i++) {
                buffer.append(String.format("%02x", record[i]));
            }
            uuid = buffer.toString();
        }
        catch (NullPointerException e) {
            uuid = "--";
        }

        addToBeaconList(major, minor, txPower, rssi, name, uuid);

    }
}
