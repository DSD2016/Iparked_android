package com.dsd2016.iparked_android.MyClasses;


public class Beacon {

    private int major;
    private int minor;
    private int rssi;
    private String name;

    public Beacon(int major, int minor, int rssi, String name, String uuid) {
        this.major = major;
        this.minor = minor;
        this.rssi = rssi;
        this.name = name;
        this.uuid = uuid;
    }

    private String uuid;

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getRssi() {return rssi;}

    public void setRssi(int rssi) {this.rssi = rssi;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {this.uuid = uuid;}


}
