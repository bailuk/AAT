package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ToDo;

public class SolidAdjustGpsAltitude extends SolidBoolean {
    private final static String KEY = "UseGpsAltitudeCorrection";

    public SolidAdjustGpsAltitude(Context c) {
        super(c, KEY);
    }



    @Override
    public String getLabel() {
        return ToDo.translate("Adjust GPS altitude");
    }
}
