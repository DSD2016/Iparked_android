package com.dsd2016.iparked_android.myClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hrvoje on 19.11.2016..
 */

public class ParcelableBeaconList implements Parcelable {
    public List<Beacon> beaconList = new ArrayList();



    public ParcelableBeaconList (List<Beacon> beaconList) {

        this.beaconList = beaconList;
    }

    public ParcelableBeaconList (Parcel parcel) {
        parcel.readTypedList(beaconList, Beacon.CREATOR);
    }

    public List<Beacon> getbeaconList() {
        return beaconList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(beaconList);
    }

    // Method to recreate a ParcelableBeaconList from a Parcel
    public static Creator<ParcelableBeaconList> CREATOR = new Creator<ParcelableBeaconList>() {

        @Override
        public ParcelableBeaconList createFromParcel(Parcel source) {
            return new ParcelableBeaconList(source);
        }

        @Override
        public ParcelableBeaconList[] newArray(int size) {
            return new ParcelableBeaconList[size];
        }

    };

}
