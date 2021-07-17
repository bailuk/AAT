package ch.bailu.aat_lib.preferences.presets;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

public class SolidAccuracyFilter extends SolidIndexList {
    private static final String KEY = "accuracy_filter_";

    private static final float[] VALUE_LIST = {
            999f,
            1f,
            2f,
            3f,
            4f,
            5f,
            10f,
            15f,
            20f,
            25f,
            30f,
            40f,
            50f,
            100f,
            200f,
    };

    private final SolidUnit sunit;

    public SolidAccuracyFilter(StorageInterface s, int i) {
        super(s, KEY + i);
        sunit = new SolidUnit(s);
    }

    public float getMinAccuracy() {
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return Res.str().p_accuracy_filter();
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }

    @Override
    public String getValueAsString(int i) {
        if (i == 0) return Res.str().off();
        return FF.f().N2.format(VALUE_LIST[i] * sunit.getAltitudeFactor())
                + sunit.getAltitudeUnit();
    }


    @Override
    public String getToolTip() {
        return Res.str().tt_p_accuracy_filter();
    }

}
