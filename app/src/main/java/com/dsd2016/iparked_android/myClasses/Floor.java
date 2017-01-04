package com.dsd2016.iparked_android.myClasses;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;


public class Floor {

    private int id;
    private int garage_id;
    private String name;
    private double longitude;
    private double latitude;
    private double angle;
    private int size_X;
    private int size_Y;
    private int zoom_level;
    private String floor_plan;
    private int floor_capacity;
    private int major_number;
    private String floor_timestamp;
    private ArrayList<JsonBeacon> beacons;

    public Floor(int id, int garage_id, String name, double longitude, double latitude, double angle, int size_X, int size_Y, int zoom_level, String floor_plan, int floor_capacity, int major_number, String floor_timestamp, ArrayList<JsonBeacon> beacons) {
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

    public Floor(int id, String name, int major_number, double angle, int size_X, int size_Y, int zoom_level, String floor_plan, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.angle = angle;
        this.size_X = size_X;
        this.size_Y = size_Y;
        this.zoom_level = zoom_level;
        this.floor_plan = floor_plan;
        this.floor_capacity = 0;
        this.major_number = major_number;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;

    }

    public int getId() {
        return id;
    }

    public int getGarageId() {
        return garage_id;
    }

    public String getName() {
        return name;
    }

    public double getAngle() {
        return angle;
    }

    public int getSizeX() {
        return size_X;
    }

    public int getSizeY() {
        return size_Y;
    }

    public int getZoomLevel() {
        return zoom_level;
    }

    public String getFloorPlan() {
        return floor_plan;
    }

    public int getMajorNumber() {
        return major_number;
    }

    public ArrayList<JsonBeacon> getBeacons() {
        return beacons;
    }
}
