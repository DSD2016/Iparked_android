package com.dsd2016.iparked_android.myClasses;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ricca on 11/12/2016.
 */

public class Floor {

    private int id;
    private int garage_id;
    private String name;
    private Double longitude;
    private Double latitude;
    private int angle;
    private int size_X;
    private int size_Y;
    private int zoom_level;
    private String floor_plan;
    private int floor_capacity;
    private int major_number;
    private String floor_timestamp;
    private ArrayList<JsonBeacon> beacons;

    public Floor(int id, int garage_id, String name, Double longitude, Double latitude, int angle, int size_X, int size_Y, int zoom_level, String floor_plan, int floor_capacity, int major_number, String floor_timestamp, ArrayList<JsonBeacon> beacons) {
        this.id = id;
        this.garage_id = garage_id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.angle = angle;
        this.size_X = size_X;
        this.size_Y = size_Y;
        this.zoom_level = zoom_level;
        this.floor_plan = floor_plan;
        this.floor_capacity = floor_capacity;
        this.major_number = major_number;
        this.floor_timestamp = floor_timestamp;
        this.beacons = beacons;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;

    }
}
