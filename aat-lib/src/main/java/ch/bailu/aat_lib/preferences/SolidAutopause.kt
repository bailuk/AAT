package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

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


    protected SolidAutopause(StorageInterface s, String key, int preset) {
        super(s, key + preset);

        sunit = new SolidUnit(s);

    }


    public float getTriggerSpeed() {
        return SPEED_VALUES[getIndex()];
    }

    public int getTriggerLevel() {
        return TRIGGER_VALUES[getIndex()];
    }


    public int getTriggerLevelMillis() {
        return TRIGGER_VALUES[getIndex()] * 1000;
    }

    public boolean isEnabled() {
        return getIndex()>0;
    }

    @Override
    public int length() {
        return SPEED_VALUES.length;
    }


    public String getValueAsString(int i) {
        if (i==0) return Res.str().off();

        return "< " +
                FF.f().N2.format(SPEED_VALUES[i] * sunit.getSpeedFactor()) +
                sunit.getSpeedUnit() + " - " +
                TRIGGER_VALUES[i] + "s";
    }

}
