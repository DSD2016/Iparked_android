package com.dsd2016.iparked_android.MyClasses;


public class Beacon {

    private int major;
    private int minor;
    private int txPower;
    private int rssi;
    private String name;
    private double distance;

    public Beacon(int major, int minor, int txPower, int rssi, String name, String uuid) {
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
        this.rssi = rssi;
        this.name = name;
        this.uuid = uuid;
        setDistance(txPower, rssi);
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

    public int getTxPower() {return txPower;}

    public void setTxPower(int txPower) {this.txPower = txPower;}

    public int getRssi() {return rssi;}

    public void setRssi(int rssi) {this.rssi = rssi;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {this.uuid = uuid;}

    public double getDistance() {return distance;}

    public void setDistance(int distance) {this.distance = distance;}

    public void setDistance(int txPower, int rssi) {
        if (rssi == 0) {
            this.distance = -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        this.distance = Math.pow(ratio, 10);
    }
}
