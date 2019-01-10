package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.general.SolidUnit;
import ch.bailu.aat.util.ToDo;

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
        return addUnit(ToDo.translate("GPS altitude modification"));
    }
}
