package com.dsd2016.iparked_android.myClasses;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.dsd2016.iparked_android.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ricca on 11/12/2016.
 */

public class Garage {

    private int id;
    private int user_id;
    private String name;
    private Double longitude;
    private Double latitude;
    private int num_floors;
    private int garage_capacity;
    private String type;
    private String uuid;
    private String city;
    private String garage_timestamp;
    private ArrayList<Floor> floors;

    public Garage(int id, int user_id, String name, Double longitude, Double latitude, int num_floors, int garage_capacity, String type, String uuid, String city, String garage_timestamp, ArrayList<Floor> floors) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.num_floors = num_floors;
        this.garage_capacity = garage_capacity;
        this.type = type;
        this.uuid = uuid;
        this.city = city;
        this.garage_timestamp = garage_timestamp;
        this.floors = floors;
    }

    // Returns the image of the specified floor.
    public Bitmap getPlan(int floor_number, final Context context){
        String url ="http://iparked-api.sytes.net/api/floorplan/" + floor_number;
        final Bitmap[] bmp = new Bitmap[1];
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        bmp[0] = bitmap;
                    }
                }, 0, 0, null,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        bmp[0] = null;
                    }
                });
// Access the RequestQueue through your singleton class.
        RestCommunicator.getInstance(context).addToRequestQueue(request);
        return bmp[0];


    }

    public LatLng getFloorLocation( int floor_number){
        double latitude = floors.get(floor_number-1).getLatitude();
        double longitude = floors.get(floor_number-1).getLongitude();

        return new LatLng(latitude, longitude);

    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
