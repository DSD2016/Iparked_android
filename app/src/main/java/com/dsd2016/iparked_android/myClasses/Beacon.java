package com.dsd2016.iparked_android.myClasses;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class Beacon implements Parcelable{


    private int major;
    private int minor;
    private String name;
    private double distance;
    private int stored; // 1 if beacon is stored in database
    private String uuid;
    private String address;
    private Location location;
    private int floorId;

    public Beacon(int major, int minor, String name, String uuid, int stored, String address) {
        this.major = major;
        this.minor = minor;
        this.name = name;
        this.uuid = uuid;
        this.stored = stored;
        this.address = address;
        this.location = null;
        this.floorId = -1;
    }

    public Beacon(int major, int minor, String name, String uuid, int stored, String address, Location location, int floorId) {
        this.major = major;
        this.minor = minor;
        this.name = name;
        this.uuid = uuid;
        this.stored = stored;
        this.address = address;
        this.location = location;
        this.floorId = floorId;
    }

    public Beacon(int major, int minor, String name, String uuid, double distance, String address, int floorId) {
        this.major = major;
        this.minor = minor;
        this.name = name;
        this.uuid = uuid;
        this.distance = distance;
        this.stored = 0;
        this.address = address;
        this.location = null;
        this.floorId = floorId;
    }

    private Beacon(Parcel in) {
        major = in.readInt();
        minor = in.readInt();
        name = in.readString();
        uuid = in.readString();
        distance = in.readDouble();
        stored = in.readInt();
        address = in.readString();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getName() {return name;}

    public String getAddress() {return address;}

    public void setName(String name) {this.name = name;}

    public String getUuid() {
        return uuid;
    }

    public double getDistance() {return distance;}

    public int getStored(){
        return this.stored;
    }

    public void setLocation(Location location) { this.location = location; }

    public Location getLocation() { return  this.location; }

    public int getFloorId(){
        return this.floorId;
    }

    public void setFloorId(int floorId){
        this.floorId = floorId;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(major);
        out.writeInt(minor);
        out.writeString(name);
        out.writeString(uuid);
        out.writeDouble(distance);
        out.writeInt(stored);
        out.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /** Method to recreate a Beacon from a Parcel */
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
