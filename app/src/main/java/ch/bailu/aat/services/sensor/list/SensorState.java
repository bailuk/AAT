package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.util.ToDo;

public final class SensorState {

    private static final int SIZE = InfoID.STEP_COUNTER_SENSOR - InfoID.HEART_RATE_SENSOR + 1;

    private static final int[] connected = new int[SIZE];

    public static final String[] NAMES = {
            ToDo.translate("Heart Rate"),
            ToDo.translate("Cadence"),
            ToDo.translate("Speed"),
            ToDo.translate("Barometer"),
            ToDo.translate("Step Counter")
    };

    public static final char[] CHARS = {
            'H', 'C', 'S', 'B', 'T'
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


    public static String getName(int iid) {
        return NAMES[toIndex(iid)];
    }
    public static char getChar(int iid) { return CHARS[toIndex(iid)]; }


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
