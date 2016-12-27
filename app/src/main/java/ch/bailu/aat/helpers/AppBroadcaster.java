package ch.bailu.aat.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import ch.bailu.aat.BuildConfig;

public class AppBroadcaster {
    public static final String NAME_SPACE= BuildConfig.APPLICATION_ID + ".";


    public static final String TILE_REMOVER_SCAN = NAME_SPACE+"TR_SCAN";
    public static final String TILE_REMOVER_STOPPED = NAME_SPACE+"TR_STOPED";
    public static final String TILE_REMOVER_REMOVE = NAME_SPACE +"TR_REMOVE";

    public static final String DBSYNC_START=NAME_SPACE+"SYNC_START";
    public static final String DBSYNC_DONE=NAME_SPACE+"SYNC_DONE";
    public static final String DB_SYNC_CHANGED=NAME_SPACE+"SYNC_CHANGED";


    /** 
     *   Information about state changes of files (in cache and on disk)
     *   
     */
    public static final String FILE_CHANGED_ONDISK  = NAME_SPACE + "ONDISK";
    public static final String FILE_CHANGED_INCACHE = NAME_SPACE + "INCACHE";
    

    /**
     *   Make a Request to the elevation updater to setTarget missing elevation information
     *   to a specific file
     */
    public static final String REQUEST_ELEVATION_UPDATE = NAME_SPACE + "REQUEST_ELEVATION_UPDATE";
    
    
    /**
     * 
     */
    //public static final String OVERLAY_CHANGED = NAME_SPACE + "OVERLAY_CHANGED";
    
    
    
    public static final String LOCATION_CHANGED = NAME_SPACE + "LOCATION";
    //public static final String DB_CURSOR_CHANGED = NAME_SPACE + "CURSOR";
    public static final String TRACKER = NAME_SPACE + "TRACKER";
    //public static final String TRACKER_STATE = NAME_SPACE + "TRACKER_STATE";

    
    
    public static final String SELECT_MAP_FEATURE = NAME_SPACE + "SELECT_MAP_FEATURE";



    public static void broadcast(Context context, String action) {
        Intent intent=new Intent();
        intent.setAction(action);


        context.sendBroadcast(intent);
    }

    public static void register(Context context, BroadcastReceiver receiver, String action) {
        IntentFilter  filter = new IntentFilter(action);


        context.registerReceiver(receiver, filter);
    }


 
    public static void broadcast(Context context, String action, String file) {
        
        Intent intent = new Intent();
        intent.setAction(action);

        AppIntent.setFile(intent, file);
        context.sendBroadcast(intent);
    }

    
    public static void broadcast(Context context, String action, String file, String url) {
        
        Intent intent = new Intent();
        intent.setAction(action);

        AppIntent.setFile(intent, file);
        AppIntent.setUrl(intent, url);
        
        context.sendBroadcast(intent);
    }


}
