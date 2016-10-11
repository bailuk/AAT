package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidTrimMode extends SolidStaticIndexList {

    public static final int MODE_TO_SIZE=0;
    public static final int MODE_TO_SIZE_AND_AGE=1;
    public static final int MODE_TO_AGE=2;
    public static final int MODE_TO_SIZE_OR_AGE=3;

    private static String[] modes = null;

    public SolidTrimMode(Context context) {
        super(Storage.global(context),
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
        return getContext().getString(R.string.p_trim_mode);
    }


    @Override
    public String getValueAsString(int i) {
        return modes[i];
    }
}
