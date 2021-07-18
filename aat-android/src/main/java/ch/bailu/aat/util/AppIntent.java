package ch.bailu.aat.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import org.mapsforge.core.model.BoundingBox;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public class AppIntent {
    private static final String EXTRA_FILE="file";
    private static final String EXTRA_URL="source";



    public static void setUrl(Intent intent, String url) {
        intent.putExtra(EXTRA_URL, url);
    }

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



    public static void setBoundingBox(Intent intent, BoundingBox box) {
        intent.putExtra("N", (int) (box.maxLatitude * 1E6d));
        intent.putExtra("E", (int) (box.maxLongitude * 1E6d));
        intent.putExtra("S", (int) (box.minLatitude * 1E6d));
        intent.putExtra("W", (int) (box.minLongitude * 1E6d));

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

    public static Intent toIntent(String action, Object[] args) {
        Intent result = new Intent(action);

        if (action == AppBroadcaster.LOG_INFO || action == AppBroadcaster.LOG_ERROR && args.length > 1) {
            String msg = args[1].toString();
            result.putExtra(AppBroadcaster.EXTRA_MESSAGE, msg);
        }
        return result;
    }

    public static Object[] toArgs(Intent intent) {
        List args = new ArrayList<>();
        addIfNotNull(args, AppIntent.getFile(intent));
        addIfNotNull(args, AppIntent.getUrl(intent));

        if (intent.hasExtra(BluetoothAdapter.EXTRA_STATE)) {
            addIfNotNull(args, intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR));
        }

        if (intent.hasExtra(AppBroadcaster.EXTRA_MESSAGE)) {
            addIfNotNull(args, intent.getStringArrayExtra(AppBroadcaster.EXTRA_MESSAGE));
        }

        return args.toArray();
    }

    private static void addIfNotNull(List list, Object obj) {
        if (obj != null) {
            list.add(obj);
        }
    }
}
