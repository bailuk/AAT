package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;

public class SolidAltitudeFromBarometer extends SolidBoolean {

    public static final String KEY = "UseBarometerForAltitude";

    public SolidAltitudeFromBarometer(Context c) {
        super(c, KEY);
    }
}
