package ch.bailu.aat.preferences.general;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidIndexList;

public class SolidPresetCount extends SolidIndexList {

    private final static String KEY=SolidPresetCount.class.getSimpleName();
    private final static int MAX=15;
    private final static int MIN=3;
    public final static int DEFAULT=5;

    public SolidPresetCount(Context c) {
        super(c, KEY);
    }

    public int getValue(int v) {
        if (v==0) return DEFAULT;

        return MIN + v - 1;
    }

    public int getValue() {
        return getValue(getIndex());
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_preset_slots);
    }

    @Override
    public String getToolTip() {
        return getString(R.string.tt_p_preset_slots);
    }


    @Override
    public int length() {
        return MAX - MIN + 2;
    }

    @Override
    protected String getValueAsString(int i) {
        String s = String.valueOf(getValue(i));
        if (i==0) s = toDefaultString(s);
        return s;
    }
}
