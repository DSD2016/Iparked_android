package com.dsd2016.iparked_android.MyClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Saeedek on 22-Nov-16.
 */

public class BeaconDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BeaconDatabaseSchema.Beacons.TABLE_NAME + " (" +
                    BeaconDatabaseSchema.Beacons._ID + " INTEGER PRIMARY KEY," +
                    BeaconDatabaseSchema.Beacons.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    BeaconDatabaseSchema.Beacons.COLUMN_VENDOR + TEXT_TYPE + COMMA_SEP +
                    BeaconDatabaseSchema.Beacons.COLUMN_UUID + TEXT_TYPE + COMMA_SEP +
                    BeaconDatabaseSchema.Beacons.COLUMN_MAJOR + TEXT_TYPE + COMMA_SEP +
                    BeaconDatabaseSchema.Beacons.COLUMN_BATTERY_CAPACITY + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BeaconDatabaseSchema.Beacons.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Beacons.db";

    public BeaconDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long Insert(String name,String vendor,String uuid,String major,String cap,Context context){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_NAME, name);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_VENDOR, vendor);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_UUID, uuid);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_MAJOR, major);
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_BATTERY_CAPACITY, cap);

        long newRowId = db.insert(BeaconDatabaseSchema.Beacons.TABLE_NAME, null, values);
        return newRowId;
    }
    public Cursor Read(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                BeaconDatabaseSchema.Beacons._ID,
                BeaconDatabaseSchema.Beacons.COLUMN_NAME,
                BeaconDatabaseSchema.Beacons.COLUMN_VENDOR,
                BeaconDatabaseSchema.Beacons.COLUMN_UUID,
                BeaconDatabaseSchema.Beacons.COLUMN_MAJOR,
                BeaconDatabaseSchema.Beacons.COLUMN_BATTERY_CAPACITY
        };
        Cursor c = db.query(
                BeaconDatabaseSchema.Beacons.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        return c;
    }
    public void Delete(String uuid){
        SQLiteDatabase db = this.getWritableDatabase();


        String selection = BeaconDatabaseSchema.Beacons.COLUMN_UUID + " LIKE ?";

        String[] selectionArgs = { uuid };

        db.delete(BeaconDatabaseSchema.Beacons.TABLE_NAME, selection, selectionArgs);
    }
    public Boolean Update(String newname,String oldname){

        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(BeaconDatabaseSchema.Beacons.COLUMN_NAME, newname);

// Which row to update, based on the title
        String selection = BeaconDatabaseSchema.Beacons.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { oldname };

        int count = db.update(
                BeaconDatabaseSchema.Beacons.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if(count==0){
            return false;
        }
        else {return true;}
    }

}
