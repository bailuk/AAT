package ch.bailu.aat_lib.service.directory.database;

public final class GpxDbConfiguration {
    public final static int DB_VERSION = 8;

    public final static String TABLE = "summary";

    public final static String KEY_FILENAME = "filename";
    public final static String KEY_DISTANCE = "distance";
    public final static String KEY_MAX_SPEED = "max_speed";
    public final static String KEY_AVG_SPEED = "avg_speed";
    public static final String KEY_PAUSE = "pause";
    public final static String KEY_TYPE_ID = "type";

    public final static String KEY_EAST_BOUNDING = "east";
    public final static String KEY_WEST_BOUNDING = "west";
    public final static String KEY_NORTH_BOUNDING = "north";
    public final static String KEY_SOUTH_BOUNDING = "south";

    public final static String KEY_ID = "_id";

    public final static String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";
    public final static String KEY_TOTAL_TIME = "total_time";

    public final static String TYPE_FLOAT = "REAL";
    public final static String TYPE_LONG = "BIGINT";
    public final static String TYPE_STRING = "TEXT";

    public final static String TYPE_ID = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public final static String TYPE_ID_H2 = "INTEGER PRIMARY KEY AUTO_INCREMENT";

    public static final String[] KEY_LIST = new String[]{
            KEY_ID,
            KEY_FILENAME,
            KEY_AVG_SPEED,
            KEY_MAX_SPEED,
            KEY_DISTANCE,
            KEY_START_TIME,
            KEY_TOTAL_TIME,
            KEY_END_TIME,
            KEY_PAUSE,
            KEY_EAST_BOUNDING,
            KEY_WEST_BOUNDING,
            KEY_NORTH_BOUNDING,
            KEY_SOUTH_BOUNDING,
            KEY_TYPE_ID
    };

    public static final String[] TYPE_LIST = new String[]{
            TYPE_ID,
            TYPE_STRING,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG
    };

    public static final String[] TYPE_LIST_H2 = new String[]{
            TYPE_ID_H2,
            TYPE_STRING,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_FLOAT,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG,
            TYPE_LONG
    };
}
