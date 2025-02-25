package ch.bailu.aat_lib.broadcaster

import ch.bailu.aat_lib.Configuration

object AppBroadcaster {
    private const val NAME_SPACE = Configuration.appId + "."
    const val TILE_REMOVER_SCAN = NAME_SPACE + "TR_SCAN"
    const val TILE_REMOVER_STOPPED = NAME_SPACE + "TR_STOPED"
    const val TILE_REMOVER_REMOVE = NAME_SPACE + "TR_REMOVE"
    const val DBSYNC_START = NAME_SPACE + "SYNC_START"
    const val DBSYNC_DONE = NAME_SPACE + "SYNC_DONE"
    const val DB_SYNC_CHANGED = NAME_SPACE + "SYNC_CHANGED"
    const val SENSOR_CHANGED = NAME_SPACE + "SENSOR_CHANGED"
    const val SENSOR_DISCONNECTED = NAME_SPACE + "SENSOR_DISCONNECTED"
    const val SENSOR_RECONNECT = NAME_SPACE + "SENSOR_RECONNECT"

    /**
     * Information about state changes of files (in cache and on disk)
     *
     */
    const val FILE_CHANGED_ONDISK = NAME_SPACE + "ONDISK"
    const val FILE_CHANGED_INCACHE = NAME_SPACE + "INCACHE"
    const val FILE_BACKGROUND_TASK_CHANGED = NAME_SPACE + "BACKGROUND_TASK"
    const val LOCATION_CHANGED = NAME_SPACE + "LOCATION"
    const val TRACKER = NAME_SPACE + "TRACKER"

    /**
     * Info level namespace for message broadcaster
     */
    const val LOG_INFO = NAME_SPACE + "LOG_INFO"

    /**
     * Error level namespace for message broadcaster
     */
    const val LOG_ERROR = NAME_SPACE + "LOG_ERROR"

    /**
     * App permission has probably been updated
     */
    const val PERMISSION_UPDATED = NAME_SPACE + "PERMISSION_UPDATED"
}
