package ch.bailu.aat_lib.dispatcher;

import ch.bailu.aat_lib.app.AppConfig;

public class AppBroadcaster {
    public static final String NAME_SPACE = AppConfig.getInstance().getApplicationId() + ".";


    public static final String TILE_REMOVER_SCAN = NAME_SPACE+"TR_SCAN";
    public static final String TILE_REMOVER_STOPPED = NAME_SPACE+"TR_STOPED";
    public static final String TILE_REMOVER_REMOVE = NAME_SPACE +"TR_REMOVE";

    public static final String DBSYNC_START=NAME_SPACE+"SYNC_START";
    public static final String DBSYNC_DONE=NAME_SPACE+"SYNC_DONE";
    public static final String DB_SYNC_CHANGED=NAME_SPACE+"SYNC_CHANGED";

    public static final String SENSOR_CHANGED = NAME_SPACE + "SENSOR_CHANGED";
    public static final String SENSOR_DISCONNECTED = NAME_SPACE + "SENSOR_DISCONNECTED";
    public static final String SENSOR_RECONNECT = NAME_SPACE + "SENSOR_RECONNECT";

    /**
     *   Information about state changes of files (in cache and on disk)
     *
     */
    public static final String FILE_CHANGED_ONDISK  = NAME_SPACE + "ONDISK";
    public static final String FILE_CHANGED_INCACHE = NAME_SPACE + "INCACHE";
    public static final String FILE_BACKGROND_TASK_CHANGED = NAME_SPACE + "BACKGROUND_TASK";
    public static final String CACHE_SYNCHRONIZED = NAME_SPACE + "CACHE_SYNCED";
    public static final String CACHE_IS_LOADED = NAME_SPACE + "CACHE_LOADED";




    public static final String LOCATION_CHANGED = NAME_SPACE + "LOCATION";
    public static final String TRACKER = NAME_SPACE + "TRACKER";


    /**
     * Info level namespace for message broadcaster
     */
    public final static String LOG_INFO = NAME_SPACE + "LOG_INFO";

    /**
     * Error level namespace for message broadcaster
     */
    public final static String LOG_ERROR = NAME_SPACE + "LOG_ERROR";

}
