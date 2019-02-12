package ch.bailu.aat.preferences.location;


import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ToDo;

public class SolidGpsTimeFix extends SolidBoolean {

    private static int checked = 0;
    private static int difference_hour = 0;

    public SolidGpsTimeFix(Context c) {
        super(c, SolidGpsTimeFix.class.getSimpleName());
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_fix_gps);
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
                return ToDo.translate("GPS timeMillis is correct (in UTC)");
            } else {
                return ToDo.translate("GPS timeMillis differs " +
                        difference_hour + " hours from system timeMillis");
            }
        }
        return null;
    }
}

