package ch.bailu.aat.util;

import android.content.Intent;

import org.mapsforge.core.model.BoundingBox;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.util.Objects;

public class AppIntent {
    private static final String EXTRA_FILE="file";
    private static final String EXTRA_URL="source";
    public final static String EXTRA_MESSAGE = "source";

    private static final String[] KEYS ={ "file", "source", "c", "d", "e", "f"};

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
        return Objects.equals(intent.getStringExtra(EXTRA_FILE), file);
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
        int size = Math.min(args.length, KEYS.length);
        Intent result = new Intent(action);

        result.putExtra("size", size);

        for (int i = 0; i < size; i++) {
            result.putExtra(KEYS[i], args[i].toString());
        }
        return result;
    }

    public static String[] toArgs(Intent intent) {
        int size = Math.min(intent.getIntExtra("size", 0), KEYS.length);

        String[] result = new String[size];

        for (int i = 0; i< size; i++) {
            result[i] = toSaveString(intent.getStringExtra(KEYS[i]));
        }
        return result;
    }

    private static String toSaveString(String stringExtra) {
        if (stringExtra == null) {
            stringExtra = "";
        }
        return stringExtra;
    }
}
