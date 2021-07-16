package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;

public class SolidAdjustGpsAltitude extends SolidBoolean {
    private final static String KEY = "UseGpsAltitudeCorrection";

    public SolidAdjustGpsAltitude(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_adjust_altitude);
    }
}
