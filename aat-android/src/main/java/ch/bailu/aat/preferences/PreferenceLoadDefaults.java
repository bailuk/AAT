package ch.bailu.aat.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.preferences.general.SolidWeight;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat_lib.preferences.OldSolidMET;

public class PreferenceLoadDefaults {
    private static final String KEY_STARTCOUNT="start_count";


    public PreferenceLoadDefaults (Activity context) {
        SolidLong startCount = new SolidLong(new Storage(context), KEY_STARTCOUNT);

        if (startCount.getValue() == 0) {
            setDefaults(context);
            AppPermission.requestFromUser(context);
        }
        startCount.setValue(startCount.getValue()+1);
    }


    private void setDefaults(Context c) {
        new SolidMapTileStack(c).setDefaults();
        new SolidWeight(new Storage(c)).setDefaults();
        OldSolidMET.setDefaults(new Storage(c));
    }


    public static long getStartCount(Context context) {
        return new SolidLong(new Storage(context), KEY_STARTCOUNT).getValue();
    }

}
