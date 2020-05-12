package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.general.SolidUnit;

public class SolidAdjustGpsAltitudeValue extends SolidAltitude {
    public static final String KEY = "AltitudeCorrection";


    public SolidAdjustGpsAltitudeValue(Context c) {
        super(c, KEY, SolidUnit.SI);
    }


    public SolidAdjustGpsAltitudeValue(Context c, int unit) {
        super(c, KEY, unit);
    }


    @Override
    public String getLabel() {
        return addUnit(getString(R.string.p_adjust_altitude_by));
    }
}
