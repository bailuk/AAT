package ch.bailu.aat.helpers;

import org.osmdroid.util.BoundingBoxE6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class AppBroadcaster {
    private static final String EXTRA_FILE="file";
    private static final String EXTRA_URL="source";
    
    private static final String EXTRA_ID="id";
    public static final String NAME_SPACE="ch.bailu.aat.";



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
     *   Make a request to the elevation updater to add missing elevation information
     *   to a specific file
     */
    public static final String REQUEST_ELEVATION_UPDATE = NAME_SPACE + "REQUEST_ELEVATION_UPDATE";
    
    
    /**
     * 
     */
    public static final String OVERLAY_CHANGED = NAME_SPACE + "OVERLAY_CHANGED";
    
    
    
    public static final String LOCATION_CHANGED = NAME_SPACE + "LOCATION";
    public static final String DB_CURSOR_CHANGED = NAME_SPACE + "CURSOR";
    public static final String TRACKER = NAME_SPACE + "TRACKER";
    public static final String TRACKER_STATE = NAME_SPACE + "TRACKER_STATE";

    
    
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


    public static void broadcast(Context context, String action, int id) {
        Intent intent = new Intent();
        intent.setAction(action);

        intent.putExtra(EXTRA_ID, id);

        context.sendBroadcast(intent);
    }


    public static void broadcast(Context context, String action, String file) {
        
        Intent intent = new Intent();
        intent.setAction(action);

        intent.putExtra(EXTRA_FILE, file);
        
        context.sendBroadcast(intent);
    }

    
    public static void broadcast(Context context, String action, String file, String url) {
        
        Intent intent = new Intent();
        intent.setAction(action);

        intent.putExtra(EXTRA_FILE, file);
        intent.putExtra(EXTRA_URL, url);
        
        context.sendBroadcast(intent);
    }
    
 
    /*
    public static void setSource(Intent intent, String source) {
        intent.putExtra(EXTRA_SOURCE, source);
    }
    */
    public static boolean hasUrl(Intent intent, String url) {
        return intent.getStringExtra(EXTRA_URL).equals(url);
    }
    
    
    public static String getUrl(Intent intent) {
        return intent.getStringExtra(EXTRA_URL);
    }
    public static void setFile(Intent intent, String file) {
        intent.putExtra(EXTRA_FILE, file);
    }
    
    public static String getFile(Intent intent) {
        return intent.getStringExtra(EXTRA_FILE);
    }
    
    public static boolean hasFile(Intent intent, String file) {
        return intent.getStringExtra(EXTRA_FILE).equals(file);
    }
    
    
    public static int getExtraID(Intent intent) {
        return intent.getIntExtra(EXTRA_ID, -1);
    }

    
    

    public static void setBoundingBox(Intent intent, BoundingBoxE6 box) {
        intent.putExtra("N", box.getLatNorthE6());
        intent.putExtra("E", box.getLonEastE6());
        intent.putExtra("S", box.getLatSouthE6());
        intent.putExtra("W", box.getLonWestE6());
        
    }
    
    public static BoundingBoxE6 getBoundingBox(Intent intent) {
        return new BoundingBoxE6(
                intent.getIntExtra("N",0),
                intent.getIntExtra("E",0),
                intent.getIntExtra("S",0),
                intent.getIntExtra("W",0)
                );
    }

    
}
