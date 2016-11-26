package com.dsd2016.iparked_android.myClasses;


import android.os.Parcel;
import android.os.Parcelable;

public class Beacon implements Parcelable{

    private int major;
    private int minor;
    private int txPower;
    private int rssi;
    private String name;
    private double distance;
    private int stored; // 1 if beacon is stored in database
    private String uuid;

    public Beacon(int major, int minor, int txPower, int rssi, String name, String uuid) {
        this.major = major;
        this.minor = minor;
        this.txPower = txPower;
        this.rssi = rssi;
        this.name = name;
        this.uuid = uuid;
        setDistance(txPower, rssi);
        this.stored = 0;
    }

    public Beacon(String name,int major, int minor,String uuid, int stored) { // Beacon creator with stored variable
        this(name,major, minor, uuid);
        this.stored = stored;
    }

    public Beacon(String uuid, int major, int minor, double distance) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.distance = distance;
    }
    public Beacon(String name, int major, int minor,String uuid) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.name = name;
    }

    public Beacon(Parcel in) {
        major = in.readInt();
        minor = in.readInt();
        txPower = in.readInt();
        rssi = in.readInt();
        name = in.readString();
        uuid = in.readString();
        distance = in.readDouble();
        stored = in.readInt();
    }



    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getTxPower() {return txPower;}

    public int getRssi() {return rssi;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUuid() {
        return uuid;
    }

    public double getDistance() {return distance;}

    public void setDistance(int txPower, int rssi) {
        if (rssi == 0) {
            this.distance = -1.0; // if we cannot determine accuracy, return -1.
        }

        double ratio = rssi*1.0/txPower;
        this.distance = Math.pow(ratio, 10);
    }

    public void setStored(int stored){
        this.stored = stored;
    }

    public int getStored(){
        return this.stored;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(major);
        out.writeInt(minor);
        out.writeInt(txPower);
        out.writeInt(rssi);
        out.writeString(name);
        out.writeString(uuid);
        out.writeDouble(distance);
        out.writeInt(stored);
    }
    @Override
    public int describeContents() {
        return 0;
    }
    // Method to recreate a Beacon from a Parcel
    public static Creator<Beacon> CREATOR = new Creator<Beacon>() {

        @Override
        public Beacon createFromParcel(Parcel source) {
            return new Beacon(source);
        }

        @Override
        public Beacon[] newArray(int size) {
            return new Beacon[size];
        }

    };
}
