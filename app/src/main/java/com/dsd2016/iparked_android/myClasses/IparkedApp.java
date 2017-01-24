package com.dsd2016.iparked_android.myClasses;

import android.app.Application;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.ArrayList;


public class IparkedApp extends Application {

    String url ="http://iparked-api.sytes.net/api/id/1";
    public static BeaconDbHelper mDbHelper;
    public static FloorDbHelper mFloorDbHelper;
    private JsonBeacon locationInGarage;
    private ArrayList<JsonBeacon> jsonBeacon = new ArrayList<>();
    private Location garageLocation = new Location("");

    @Override
    public void onCreate() {
        super.onCreate();
        // Required initialization logic here!
        locationInGarage = new JsonBeacon();
        mFloorDbHelper = new FloorDbHelper(getApplicationContext());
        mDbHelper = new BeaconDbHelper(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Garage garage = gson.fromJson(response, Garage.class);
                garageLocation.setLatitude(garage.getLatitude());
                garageLocation.setLongitude(garage.getLongitude());
                for (Floor f:garage.getFloors()){
                    for (JsonBeacon b:f.getBeacons()){
                        jsonBeacon.add(b);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IparkedApp.this, "Error downloading garage beacons", Toast.LENGTH_SHORT).show();
            }
        });
        RestCommunicator.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public Location getGarageLocation() {
        return garageLocation;
    }

    public void setGarageLocation(Location garageLocation) {
        this.garageLocation = garageLocation;
    }

    public ArrayList<JsonBeacon> getJsonBeacon() {
        return jsonBeacon;
    }

    public void setJsonBeacon(ArrayList<JsonBeacon> jsonBeacon) {
        this.jsonBeacon = jsonBeacon;
    }

    public JsonBeacon getLocationInGarage() {
        return locationInGarage;
    }

    public void setLocationInGarage(JsonBeacon locationInGarage) {
        this.locationInGarage = locationInGarage;
    }
}
