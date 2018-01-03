package ch.bailu.aat.preferences;


import android.content.Context;

import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.AppLog;

public class SolidGpsTimeFix extends SolidBoolean {

    private static int fixed = 0;
    private static int difference_hour = 0;

    public SolidGpsTimeFix(Context c) {
        super(Storage.global(c), SolidGpsTimeFix.class.getSimpleName());
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Fix GPS time");
    }


    public static long fix(long gpsTime, long systemTime) {
        long lfix = systemTime - gpsTime;

        float ffix = lfix / (1000 * 60);
        difference_hour = Math.round(ffix / 60f);

        if (difference_hour == 0) {
            AppLog.d(new Object(), "GPS time is correct (in UTC)");
            return gpsTime;

        } else {
            fixed ++;

            AppLog.d(new Object(), "GPS time differs " + difference_hour + " hour from system time");
            lfix = difference_hour * 1000 * 60 * 60;
            return gpsTime + lfix;
        }
    }
}

