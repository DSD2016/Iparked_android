package com.dsd2016.iparked_android.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if ( intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) ) {

            /** Start service if app has Bluetooth permission */
            SharedPreferences pref = context.getSharedPreferences("iParked_preferences", Context.MODE_PRIVATE);
            if (!pref.getBoolean("first_boot", true)) {
                context.startService(new Intent(context, BeaconProximityService.class));
            }

        }

    }
}
