package com.dsd2016.iparked_android.Services;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class OnBluetoothReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch(state) {

                /** Stop proximity service if it is active */
                case BluetoothAdapter.STATE_TURNING_OFF:
                    context.stopService(new Intent(context, BeaconProximityService.class));
                    Log.v("iParked", "Bluetooth off");
                    break;

                /** Start proximity service if it already isn't active */
                case BluetoothAdapter.STATE_ON:
                    ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
                    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                        if (BeaconProximityService.class.getName().equals(service.service.getClassName())) {
                            context.startService(new Intent(context, BeaconProximityService.class));
                        }
                    }
                    break;
            }

        }
    }

}
