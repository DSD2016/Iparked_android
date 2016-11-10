package com.dsd2016.iparked_android.Services;

import android.app.IntentService;
import android.content.Intent;

public class BeaconProximityService extends IntentService {

    public BeaconProximityService() {
        super("BeaconProximity");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        String dataString = workIntent.getDataString();
    }
}
