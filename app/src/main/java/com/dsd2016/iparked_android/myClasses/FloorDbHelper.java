package com.dsd2016.iparked_android.myClasses;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FloorDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FloorDatabaseSchema.Floors.TABLE_NAME + " (" +
                    FloorDatabaseSchema.Floors._ID + " INTEGER PRIMARY KEY," +
                    FloorDatabaseSchema.Floors.COLUMN_NAME + " TEXT," +
                    FloorDatabaseSchema.Floors.COLUMN_MAJOR + " INTEGER," +
                    FloorDatabaseSchema.Floors.COLUMN_ANGLE + " DOUBLE," +
                    FloorDatabaseSchema.Floors.COLUMN_SIZE_X + " INTEGER," +
                    FloorDatabaseSchema.Floors.COLUMN_SIZE_Y + " INTEGER," +
                    FloorDatabaseSchema.Floors.COLUMN_ZOOM_LEVEL + " INTEGER," +
                    FloorDatabaseSchema.Floors.COLUMN_FLOOR_PLAN + " TEXT," +
                    FloorDatabaseSchema.Floors.COLUMN_LOCATION_LAT + " DOUBLE," +
                    FloorDatabaseSchema.Floors.COLUMN_LOCATION_LON + " DOUBLE" +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FloorDatabaseSchema.Floors.TABLE_NAME;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Beacons.db";

    /** Indexes of columns */
    private static int INDEX_ID;
    private static int INDEX_NAME;
    private static int INDEX_MAJOR;
    private static int INDEX_ANGLE;
    private static int INDEX_SIZE_X;
    private static int INDEX_SIZE_Y;
    private static int INDEX_ZOOM_LEVEL;
    private static int INDEX_FLOOR_PLAN;
    private static int INDEX_LOCATION_LON;
    private static int INDEX_LOCATION_LAT;


    FloorDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        //db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void getIndexes() {
        Cursor cursor = read();

        INDEX_ID = cursor.getColumnIndex(FloorDatabaseSchema.Floors._ID);
        INDEX_NAME = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_NAME);
        INDEX_MAJOR = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_MAJOR);
        INDEX_ANGLE = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_ANGLE);
        INDEX_SIZE_X = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_SIZE_X);
        INDEX_SIZE_Y = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_SIZE_Y);
        INDEX_ZOOM_LEVEL = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_ZOOM_LEVEL);
        INDEX_FLOOR_PLAN = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_FLOOR_PLAN);
        INDEX_LOCATION_LAT = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_LOCATION_LAT);
        INDEX_LOCATION_LON = cursor.getColumnIndex(FloorDatabaseSchema.Floors.COLUMN_LOCATION_LON);
    }

    public long insertOrUpdate (Floor floor) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FloorDatabaseSchema.Floors._ID, floor.getId());
        values.put(FloorDatabaseSchema.Floors.COLUMN_NAME, floor.getName());
        values.put(FloorDatabaseSchema.Floors.COLUMN_MAJOR, floor.getMajorNumber());
        values.put(FloorDatabaseSchema.Floors.COLUMN_ANGLE, floor.getAngle());
        values.put(FloorDatabaseSchema.Floors.COLUMN_SIZE_X, floor.getSizeX());
        values.put(FloorDatabaseSchema.Floors.COLUMN_SIZE_Y, floor.getSizeY());
        values.put(FloorDatabaseSchema.Floors.COLUMN_ZOOM_LEVEL, floor.getZoomLevel());
        values.put(FloorDatabaseSchema.Floors.COLUMN_FLOOR_PLAN, floor.getFloorPlan());
        values.put(FloorDatabaseSchema.Floors.COLUMN_LOCATION_LAT, floor.getLatitude());
        values.put(FloorDatabaseSchema.Floors.COLUMN_LOCATION_LON, floor.getLongitude());

        if(floor.getId() != -1){
            return db.replace(FloorDatabaseSchema.Floors.TABLE_NAME, null, values);
        }
        else {
            return -1;
        }
    }

    public Cursor read () {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                FloorDatabaseSchema.Floors._ID,
                FloorDatabaseSchema.Floors.COLUMN_NAME,
                FloorDatabaseSchema.Floors.COLUMN_MAJOR,
                FloorDatabaseSchema.Floors.COLUMN_ANGLE,
                FloorDatabaseSchema.Floors.COLUMN_SIZE_X,
                FloorDatabaseSchema.Floors.COLUMN_SIZE_Y,
                FloorDatabaseSchema.Floors.COLUMN_ZOOM_LEVEL,
                FloorDatabaseSchema.Floors.COLUMN_FLOOR_PLAN,
                FloorDatabaseSchema.Floors.COLUMN_LOCATION_LAT,
                FloorDatabaseSchema.Floors.COLUMN_LOCATION_LON,
        };

        // TODO fix properly
        return db.query(
                FloorDatabaseSchema.Floors.TABLE_NAME, projection, null, null, null, null, null
        );

    }

    public Cursor readById(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {
                FloorDatabaseSchema.Floors._ID,
                FloorDatabaseSchema.Floors.COLUMN_NAME,
                FloorDatabaseSchema.Floors.COLUMN_MAJOR,
                FloorDatabaseSchema.Floors.COLUMN_ANGLE,
                FloorDatabaseSchema.Floors.COLUMN_SIZE_X,
                FloorDatabaseSchema.Floors.COLUMN_SIZE_Y,
                FloorDatabaseSchema.Floors.COLUMN_ZOOM_LEVEL,
                FloorDatabaseSchema.Floors.COLUMN_FLOOR_PLAN,
                FloorDatabaseSchema.Floors.COLUMN_LOCATION_LAT,
                FloorDatabaseSchema.Floors.COLUMN_LOCATION_LON,
        };

        String where = FloorDatabaseSchema.Floors._ID + "=" + id;

        // TODO fix properly
        return db.query(
                FloorDatabaseSchema.Floors.TABLE_NAME, projection, where, null, null, null, null
        );

    }

    public Boolean delete (int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FloorDatabaseSchema.Floors._ID + " LIKE ?";
        String[] selectionArgs = { Integer.toString(id) };

        int delete = db.delete(FloorDatabaseSchema.Floors.TABLE_NAME, selection, selectionArgs);

        return delete != 0;
    }


    public Floor getFloor(int id) {

        Cursor floorCursor = this.readById(id);
        floorCursor.moveToFirst();

        if (floorCursor.getCount() == 0) {
            return null;
        }

        getIndexes();
        Floor newFloor = new Floor(
                floorCursor.getInt(INDEX_ID),
                floorCursor.getString(INDEX_NAME),
                floorCursor.getInt(INDEX_MAJOR),
                floorCursor.getDouble(INDEX_ANGLE),
                floorCursor.getInt(INDEX_SIZE_X),
                floorCursor.getInt(INDEX_SIZE_Y),
                floorCursor.getInt(INDEX_ZOOM_LEVEL),
                floorCursor.getString(INDEX_FLOOR_PLAN),
                floorCursor.getDouble(INDEX_LOCATION_LAT),
                floorCursor.getDouble(INDEX_LOCATION_LON)
        );
        return newFloor;
    }

}
