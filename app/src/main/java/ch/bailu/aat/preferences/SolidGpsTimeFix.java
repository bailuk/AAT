package ch.bailu.aat.preferences;


import android.content.Context;

import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.ui.AppLog;

public class SolidGpsTimeFix extends SolidBoolean {

    private static int checked = 0;
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

        checked++;
        if (difference_hour == 0) {
            return gpsTime;

        } else {
            lfix = difference_hour * 1000 * 60 * 60;
            return gpsTime + lfix;
        }
    }

    @Override
    public String getToolTip() {
        if (checked > 0) {

            if (difference_hour == 0) {
                return ToDo.translate("GPS time is correct (in UTC)");
            } else {
                return ToDo.translate("GPS time differs " +
                        difference_hour + " hours from system time");
            }
        }
        return null;
    }
}

