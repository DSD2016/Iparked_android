package com.dsd2016.iparked_android.myClasses;

/**
 * Created by ricca on 11/12/2016.
 */

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
}
