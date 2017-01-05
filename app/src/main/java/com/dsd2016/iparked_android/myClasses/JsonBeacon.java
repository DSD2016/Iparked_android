package com.dsd2016.iparked_android.myClasses;


public class JsonBeacon {

    /*id": 1,
			"floor_id": 1,
			"name": "HMSoft1",
			"latitude": 45.4,
			"longitude": 15.4,
			"minor_number": 5,
			"bluetooth_adress": "20:C3:8F:F2:C0:66"*/


    private int id;
    private int floor_id;
    private String name;
    private Double latitude;
    private Double longitude;
    private int minor_number;
    private String bluetooth_address;

    public JsonBeacon(int id, int floor_id, String name, Double latitude, Double longitude, int minor_number, String bluetooth_address) {
        this.id = id;
        this.floor_id = floor_id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.minor_number = minor_number;
        this.bluetooth_address = bluetooth_address;
    }

    public JsonBeacon() {
        this.id = 0;
        this.floor_id = 0;
        this.name = null;
        this.latitude = null;
        this.longitude = null;
        this.minor_number = 0;
        this.bluetooth_address = null;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getFloor_id() {
        return floor_id;
    }

    public String getName() {
        return name;
    }

    public int getMinor_number() {
        return minor_number;
    }
    public String getBluetooth_address(){ return bluetooth_address;}

    public void setFloor_id(int floor_id) {
        this.floor_id = floor_id;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
