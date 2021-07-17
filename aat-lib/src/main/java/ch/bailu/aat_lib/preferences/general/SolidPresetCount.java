package ch.bailu.aat_lib.preferences.general;

import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidPresetCount extends SolidIndexList {

    private final static String KEY=SolidPresetCount.class.getSimpleName();
    public final static int MAX=15;
    private final static int MIN=3;
    public final static int DEFAULT=5;

    public SolidPresetCount(StorageInterface s) {
        super(s, KEY);
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
        return Res.str().p_preset_slots();
    }

    @Override
    public String getToolTip() {
        return Res.str().tt_p_preset_slots();
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
