package ch.bailu.aat.preferences;

import android.content.Context;
import android.view.View;

public class SolidMapViewAcceleration extends SolidIndexList {

    private static int[] values = {
            View.LAYER_TYPE_HARDWARE,
            View.LAYER_TYPE_SOFTWARE,
            View.LAYER_TYPE_NONE
    };

    private static String[] labels = {
            "Hardware*",
            "Software*",
            "None*"
    };


    public SolidMapViewAcceleration(Context c) {
        super(  Storage.global(c),
                SolidMapViewAcceleration.class.getSimpleName());
    }

    @Override
    public int length() {
        return values.length;
    }

    @Override
    protected String getValueAsString(int i) {
        return labels[i];
    }

    public int getValue() {
        return values[getIndex()];
    }

    @Override
    public String getLabel() {
        return "Acceleration*";
    }
}
