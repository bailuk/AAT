package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.util.ToDo;

public class SolidPresetCount extends SolidInteger {

    private final static String KEY=SolidPresetCount.class.getSimpleName();
    private final static int MAX=15;
    private final static int MIN=3;
    public final static int DEFAULT=5;

    public SolidPresetCount(Context c) {
        super(Storage.global(c), KEY);
    }

    @Override
    public int getValue() {
        return limit(super.getValue());
    }


    @Override
    public void setValue(int v) {
        super.setValue(limit(v));
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Activity slots");
    }

    @Override
    public String getToolTip() {
        return ToDo.translate(
                "Number of activity slots you want to use " +
                        "to organize tracks. " +
                        "Value from " + MIN + " to " + MAX+".");
    }

    private int limit(int value) {
        if (value == 0) value = DEFAULT;
        else if (value < MIN) value =  MIN;
        else if (value > MAX) value =  MAX;
        return value;
    }
}
