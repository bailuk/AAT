package ch.bailu.aat.preferences.location;


import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ToDo;

public class SolidGpsTimeFix extends SolidBoolean {

    private static int checked = 0;
    private static long differenceHour = 0;
    private static long differenceMillis = 0;

    public SolidGpsTimeFix(Context c) {
        super(c, SolidGpsTimeFix.class.getSimpleName());
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_fix_gps);
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
                return ToDo.translate("GPS time is correct (in UTC)");
            } else {
                return ToDo.translate("GPS time differs " +
                        differenceHour + " hours from system time");
            }
        }
        return null;
    }
}

