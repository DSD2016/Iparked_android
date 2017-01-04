package com.dsd2016.iparked_android.myClasses;

import android.provider.BaseColumns;

public final class FloorDatabaseSchema {
    private FloorDatabaseSchema() {}

    public static class Floors implements BaseColumns {
        public static final String TABLE_NAME = "floor";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_MAJOR = "major";
        public static final String COLUMN_ANGLE = "angle";
        public static final String COLUMN_SIZE_X = "sizeX";
        public static final String COLUMN_SIZE_Y = "sizeY";
        public static final String COLUMN_ZOOM_LEVEL = "zoomLevel";
        public static final String COLUMN_FLOOR_PLAN = "floorPlan";
        public static final String COLUMN_LOCATION_LAT = "latitude";
        public static final String COLUMN_LOCATION_LON = "longitude";
    }

}
