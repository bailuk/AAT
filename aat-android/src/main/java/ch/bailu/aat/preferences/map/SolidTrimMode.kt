package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.resources.Res;

public class SolidTrimMode extends SolidStaticIndexList {

    public static final int MODE_TO_SIZE=0;
    public static final int MODE_TO_SIZE_AND_AGE=1;
    public static final int MODE_TO_AGE=2;
    public static final int MODE_TO_SIZE_OR_AGE=3;

    private static String[] modes = null;

    public SolidTrimMode(Context context) {
        super(new Storage(context),
                SolidTrimMode.class.getSimpleName(),
                generateModes(context));

    }


    public static String[] generateModes(Context c) {
        if (modes == null) {
            modes = c.getResources().getStringArray(R.array.p_trim_modes);
        }
        return modes;
    }


    @Override
    public int length() {
        return modes.length;
    }


    @Override
    public String getLabel() {
        return Res.str().p_trim_mode();
    }


    @Override
    public String getValueAsString(int i) {
        return modes[i];
    }
}
