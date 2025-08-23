package ch.bailu.aat_lib.service.directory.database

object GpxDbConfiguration {
    const val DB_VERSION: Int = 8

    const val TABLE: String = "summary"

    const val KEY_FILENAME: String = "filename"
    const val KEY_DISTANCE: String = "distance"
    const val KEY_MAX_SPEED: String = "max_speed"
    const val KEY_AVG_SPEED: String = "avg_speed"
    const val KEY_PAUSE: String = "pause"
    const val KEY_TYPE_ID: String = "type"

    const val KEY_EAST_BOUNDING: String = "east"
    const val KEY_WEST_BOUNDING: String = "west"
    const val KEY_NORTH_BOUNDING: String = "north"
    const val KEY_SOUTH_BOUNDING: String = "south"

    const val KEY_ID: String = "_id"

    const val KEY_START_TIME: String = "start_time"
    const val KEY_END_TIME: String = "end_time"
    const val KEY_TOTAL_TIME: String = "total_time"

    private const val TYPE_FLOAT: String = "REAL"
    private const val TYPE_LONG: String = "BIGINT"
    private const val TYPE_STRING: String = "TEXT"

    private const val TYPE_ID: String = "INTEGER PRIMARY KEY AUTOINCREMENT"
    private const val TYPE_ID_H2: String = "INTEGER PRIMARY KEY AUTO_INCREMENT"

    val KEY_LIST: Array<String> = arrayOf(
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
