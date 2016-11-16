package com.dsd2016.iparked_android.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ( intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) ) {
            Log.v("iParked", "OnBootReceiver - got: " + intent.getAction().toString());
            context.startService(new Intent(context, BeaconProximityService.class));
        }
    }
}
