package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public abstract class SolidAutopause extends SolidIndexList {

    private static final float[] SPEED_VALUES = {0,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
            0.25f, 0.50f, 0.75f, 1.00f, 1.25f, 1.50f,
    };


    private static final int[] TRIGGER_VALUES = {0,
            3 ,3 ,3 ,3 ,3 ,3,
            4 ,4 ,4 ,4 ,4 ,4,
            5 ,5 ,5 ,5 ,5 ,5,
            10,10,10,10,10,10,
            20,20,20,20,20,20,
    };

    private final SolidUnit sunit;


    protected SolidAutopause(Context c, String key, int preset) {
        super(Storage.preset(c), key + preset);

        sunit = new SolidUnit(c);

    }


    public float getTriggerSpeed() {
        return SPEED_VALUES[getIndex()];
    }

    public int getTriggerLevel() {
        return TRIGGER_VALUES[getIndex()];
    }


    public boolean isEnabled() {
        return getIndex()>0;
    }

    @Override
    public int length() {
        return SPEED_VALUES.length;
    }


    public String getValueAsString(int i) {
        if (i==0) return getContext().getString(R.string.off);

        return String.format("%d * < %.3f%s", TRIGGER_VALUES[i], SPEED_VALUES[i] * sunit.getSpeedFactor(), sunit.getSpeedUnit());
    }

}
