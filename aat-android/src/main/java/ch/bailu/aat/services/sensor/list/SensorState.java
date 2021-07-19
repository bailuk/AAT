package ch.bailu.aat.services.sensor.list;

import android.content.Context;

import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.resources.Res;

public final class SensorState {

    private static final int SIZE = InfoID.STEP_COUNTER_SENSOR - InfoID.HEART_RATE_SENSOR + 1;

    private static final int[] connected = new int[SIZE];

    private static final String[] NAMES = {
            Res.str().sensor_heart_rate(),
            Res.str().sensor_power(),
            Res.str().sensor_cadence(),
            Res.str().sensor_speed(),
            Res.str().sensor_barometer(),
            Res.str().sensor_step_counter()
    };

    private static final char[] CHARS = {
            'H', 'P', 'C', 'S', 'B', 'T'
    };


    public static int toIndex(int iid) {
        return iid-InfoID.HEART_RATE_SENSOR;
    }

    public static boolean isConnected(int iid) {
        return connected[toIndex(iid)] > 0;
    }


    public static void connect(int iid) {
        connected[toIndex(iid)]++;
    }

    public static void disconnect(int iid) {
        connected[toIndex(iid)]--;
    }


    public static String getName(Context c, int iid) {
        return NAMES[toIndex(iid)];
    }
    
    public static String getOverviewString() {
        StringBuilder overview= new StringBuilder();
        for (int i = 0; i< SIZE; i++) {
            if (connected[i] > 0) {
                overview.append(CHARS[i]).append(connected[i]);
            }
        }

        return overview.toString();
    }
}
