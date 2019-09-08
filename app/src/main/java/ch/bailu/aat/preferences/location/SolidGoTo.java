package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.util.ToDo;

public class SolidGoTo extends SolidString {
    private final static String KEY = "GoToLocation";
    public SolidGoTo(Context c) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Center map at location (Geo URL or plus code):");
    }
}
