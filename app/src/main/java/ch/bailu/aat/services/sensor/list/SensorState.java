package ch.bailu.aat.services.sensor.list;

import ch.bailu.aat.gpx.InfoID;

public class SensorState {

    private static int SIZE = InfoID.BAROMETER_SENSOR - InfoID.HEART_RATE_SENSOR + 1;

    private static int[] connected = new int[SIZE];

    public static final String[] NAMES = {
            "Heart Rate",
            "Cadence",
            "Speed",
            "Barometer",
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


    public static String getOverviewString() {
        String overview="";
        for (int i = 0; i< SIZE; i++) {
            if (connected[i] > 0) {
                overview = overview
                        + NAMES[i].charAt(0)
                        + String.valueOf(connected[i]);
            }
        }

        return overview;
    }
}
