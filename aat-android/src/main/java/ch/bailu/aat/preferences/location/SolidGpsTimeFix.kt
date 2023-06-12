package ch.bailu.aat.preferences.location;


import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidGpsTimeFix extends SolidBoolean {

    private static int checked = 0;
    private static long differenceHour = 0;
    private static long differenceMillis = 0;

    public SolidGpsTimeFix(StorageInterface s) {
        super(s, SolidGpsTimeFix.class.getSimpleName());
    }

    @Override
    public String getLabel() {
        return Res.str().p_fix_gps();
    }


    public static long fix(long gpsTime, long systemTime) {
        if (checked < 5) {
            differenceHour = getDifferenceHour(gpsTime, systemTime);
            differenceMillis = getMillisFromHour(differenceHour);
            checked ++;
        }

        if (differenceHour == 0) {
            return gpsTime;

        } else {
            return gpsTime + differenceMillis;
        }
    }


    public static long getMillisFromHour(long hour) {
        return hour * 1000L * 60L * 60L;
    }

    public static long getDifferenceHour(long gpsTime, long systemTime) {
        long millis = systemTime - gpsTime;
        long seconds = millis / 1000L;
        double minutes = seconds / 60d;
        return Math.round(minutes / 60d);
    }


    @Override
    public String getToolTip() {
        if (checked > 0) {

            if (differenceHour == 0) {
                return Res.str().p_fix_correct();
            } else {
                return Res.str().p_fix_differs() + " "  + differenceHour;
            }
        }
        return null;
    }
}

