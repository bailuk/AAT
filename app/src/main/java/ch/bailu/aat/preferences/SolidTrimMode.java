package ch.bailu.aat.preferences;

import android.content.Context;

public class SolidTrimMode extends SolidIndexList {

    public static final int MODE_TO_SIZE=0;
    public static final int MODE_TO_SIZE_AND_AGE=1;
    public static final int MODE_TO_AGE=2;
    public static final int MODE_TO_SIZE_OR_AGE=3;

    private final String[] modes = {
            "Trim to size*",
            "Trim to size and age*",
            "Trim to age*",
            "Trim to size or age*"
    };

    public SolidTrimMode(Context context) {
        super(Storage.global(context), SolidTrimMode.class.getSimpleName());
    }

    @Override
    public int length() {
        return modes.length;
    }


    @Override
    public String getLabel() {
        return "Trim mode*";
    }


    @Override
    public String getValueAsString(int i) {
        return modes[i];
    }
}
