package ch.bailu.aat.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.util.AppPermission;
import ch.bailu.aat_lib.preferences.OldSolidMET;
import ch.bailu.aat_lib.preferences.SolidLong;
import ch.bailu.aat_lib.preferences.general.SolidWeight;
import ch.bailu.foc_android.FocAndroidFactory;

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
        SolidRenderTheme stheme = new SolidRenderTheme(new AndroidSolidMapsForgeDirectory(c), new FocAndroidFactory(c));
        new SolidMapTileStack(stheme).setDefaults();
        new SolidWeight(new Storage(c)).setDefaults();
        OldSolidMET.setDefaults(new Storage(c));
    }


    public static long getStartCount(Context context) {
        return new SolidLong(new Storage(context), KEY_STARTCOUNT).getValue();
    }

}
