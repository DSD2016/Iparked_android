package com.dsd2016.iparked_android.myClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;


public class BeaconDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BeaconDatabaseSchema.Beacons.TABLE_NAME + " (" +
                    BeaconDatabaseSchema.Beacons._ID + " INTEGER PRIMARY KEY," +
                    BeaconDatabaseSchema.Beacons.COLUMN_NAME + " TEXT," +
                    BeaconDatabaseSchema.Beacons.COLUMN_UUID + " TEXT," +
                    BeaconDatabaseSchema.Beacons.COLUMN_MAJOR + " INTEGER," +
                    BeaconDatabaseSchema.Beacons.COLUMN_MINOR + " INTEGER," +
                    BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS + " TEXT," +
                    BeaconDatabaseSchema.Beacons.COLUMN_STORED + " INTEGER," +
                    BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT + " DOUBLE," +
                    BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON + " DOUBLE" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BeaconDatabaseSchema.Beacons.TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Beacons.db";

    /** Indexes of columns */
    private static int INDEX_ID;
    private static int INDEX_NAME;
    private static int INDEX_UUID;
    private static int INDEX_MAJOR;
    private static int INDEX_MINOR;
    private static int INDEX_ADDRESS;
    private static int INDEX_STORED;
    private static int INDEX_LOCATION_LON;
    private static int INDEX_LOCATION_LAT;


    public BeaconDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void getIndexes() {
        Cursor cursor = read();

        INDEX_ID = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons._ID);
        INDEX_NAME = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_MINOR);
        INDEX_UUID = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_UUID);
        INDEX_MAJOR = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_MAJOR);
        INDEX_MINOR = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_MINOR);
        INDEX_ADDRESS = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS);
        INDEX_NAME = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_NAME);
        INDEX_STORED = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_STORED);
        INDEX_LOCATION_LAT = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT);
        INDEX_LOCATION_LON = cursor.getColumnIndex(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON);
    }

    public long insert (String name, int major, int minor, String uuid, String address, Location location) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_NAME, name);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_UUID, uuid);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_MAJOR, major);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_MINOR, minor);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS, address);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_STORED, 1);

        if (location != null) {
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT, location.getLatitude());
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON, location.getLongitude());
        }

        return db.insert(BeaconDatabaseSchema.Beacons.TABLE_NAME, null, values);
    }

    public Cursor read () {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                BeaconDatabaseSchema.Beacons._ID,
                BeaconDatabaseSchema.Beacons.COLUMN_NAME,
                BeaconDatabaseSchema.Beacons.COLUMN_UUID,
                BeaconDatabaseSchema.Beacons.COLUMN_MAJOR,
                BeaconDatabaseSchema.Beacons.COLUMN_MINOR,
                BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS,
                BeaconDatabaseSchema.Beacons.COLUMN_STORED,
                BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT,
                BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON,
        };

        // TODO fix properly)
        return db.query(
                BeaconDatabaseSchema.Beacons.TABLE_NAME, projection, null, null, null, null, null
        );

    }

    public Boolean delete (String address) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS + " LIKE ?";
        String[] selectionArgs = { address };

        int delete = db.delete(BeaconDatabaseSchema.Beacons.TABLE_NAME, selection, selectionArgs);

        return delete != 0;
    }

    public Boolean update (String newname, String address) {

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_NAME, newname);

        /** Which row to update, based on the title */
        String selection = BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS + " LIKE ?";
        String[] selectionArgs = { address };

        int count = db.update(
                BeaconDatabaseSchema.Beacons.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count != 0;
    }

    public Boolean updateBeaconLocation (Beacon beacon) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (beacon.getLocation() != null) {
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT, beacon.getLocation().getLatitude());
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON, beacon.getLocation().getLongitude());
        } else {
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LAT, 0.0);
            values.put(BeaconDatabaseSchema.Beacons.COLUMN_LOCATION_LON, 0.0);
        }

        /** Which row to update, based on the title */
        String selection = BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS + " LIKE ?";
        String[] selectionArgs = { beacon.getAddress() };

        int count = db.update(
                BeaconDatabaseSchema.Beacons.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count != 0;
    }

    /** Check if beacon exists in database */
    public ArrayList<Beacon> getPersonalBeacons() {

        ArrayList<Beacon> personalBeaconList = new ArrayList<>();
        Cursor beaconCursor = this.read();
        beaconCursor.moveToFirst();

        if (beaconCursor.getCount() == 0) {
            return null;
        }

        getIndexes();
        do {
            Location location = new Location("");
            location.setLatitude(beaconCursor.getDouble(INDEX_LOCATION_LAT));
            location.setLongitude(beaconCursor.getDouble(INDEX_LOCATION_LON));
            Beacon newBeacon = new Beacon(
                    beaconCursor.getInt(INDEX_MAJOR),
                    beaconCursor.getInt(INDEX_MINOR),
                    beaconCursor.getString(INDEX_NAME),
                    beaconCursor.getString(INDEX_UUID),
                    beaconCursor.getInt(INDEX_STORED),
                    beaconCursor.getString(INDEX_ADDRESS),
                    location
                    );
            personalBeaconList.add(newBeacon);
        } while (beaconCursor.moveToNext());
        return personalBeaconList;
    }

}
