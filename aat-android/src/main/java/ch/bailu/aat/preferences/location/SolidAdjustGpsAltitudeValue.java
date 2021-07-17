package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

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
        return Res.str().p_adjust_altitude_by();
    }
}
