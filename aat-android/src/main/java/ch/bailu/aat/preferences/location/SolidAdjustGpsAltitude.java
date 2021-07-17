package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.resources.Res;

public class SolidAdjustGpsAltitude extends SolidBoolean {
    private final static String KEY = "UseGpsAltitudeCorrection";

    public SolidAdjustGpsAltitude(Context c) {
        super(new Storage(c), KEY);
    }

    @Override
    public String getLabel() {
        return Res.str().p_adjust_altitude();
    }
}
