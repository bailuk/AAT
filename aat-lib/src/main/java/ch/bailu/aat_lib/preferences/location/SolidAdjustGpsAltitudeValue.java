package ch.bailu.aat_lib.preferences.location;

import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

public class SolidAdjustGpsAltitudeValue extends SolidAltitude {
    public static final String KEY = "AltitudeCorrection";


    public SolidAdjustGpsAltitudeValue(StorageInterface s) {
        super(s, KEY, SolidUnit.SI);
    }


    public SolidAdjustGpsAltitudeValue(StorageInterface s, int unit) {
        super(s, KEY, unit);
    }


    @Override
    public String getLabel() {
        return Res.str().p_adjust_altitude_by();
    }
}
