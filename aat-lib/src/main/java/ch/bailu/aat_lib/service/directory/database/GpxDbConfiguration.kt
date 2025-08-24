package ch.bailu.aat_lib.service.directory.database

object GpxDbConfiguration {
    const val DB_VERSION: Int = 8

    const val TABLE: String = "summary"

    const val ATTR_FILENAME: String = "filename"
    const val ATTR_DISTANCE: String = "distance"
    const val ATTR_MAX_SPEED: String = "max_speed"
    const val ATTR_AVG_SPEED: String = "avg_speed"
    const val ATTR_PAUSE: String = "pause"
    const val ATTR_TYPE_ID: String = "type"

    const val ATTR_EAST_BOUNDING: String = "east"
    const val ATTR_WEST_BOUNDING: String = "west"
    const val ATTR_NORTH_BOUNDING: String = "north"
    const val ATTR_SOUTH_BOUNDING: String = "south"

    const val ATTR_ID: String = "_id"

    const val ATTR_START_TIME: String = "start_time"
    const val ATTR_END_TIME: String = "end_time"
    const val ATTR_TOTAL_TIME: String = "total_time"

    private const val TYPE_FLOAT: String = "REAL"
    private const val TYPE_LONG: String = "BIGINT"
    private const val TYPE_STRING: String = "TEXT"

    private const val TYPE_ID: String = "INTEGER PRIMARY KEY AUTOINCREMENT"
    private const val TYPE_ID_H2: String = "INTEGER PRIMARY KEY AUTO_INCREMENT"

    val ATTR_LIST: Array<String> = arrayOf(
        ATTR_ID,
        ATTR_FILENAME,
        ATTR_AVG_SPEED,
        ATTR_MAX_SPEED,
        ATTR_DISTANCE,
        ATTR_START_TIME,
        ATTR_TOTAL_TIME,
        ATTR_END_TIME,
        ATTR_PAUSE,
        ATTR_EAST_BOUNDING,
        ATTR_WEST_BOUNDING,
        ATTR_NORTH_BOUNDING,
        ATTR_SOUTH_BOUNDING,
        ATTR_TYPE_ID
    )

    val TYPE_LIST: Array<String> = arrayOf(
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
    )

    val TYPE_LIST_H2: Array<String> = arrayOf(
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
    )
}
