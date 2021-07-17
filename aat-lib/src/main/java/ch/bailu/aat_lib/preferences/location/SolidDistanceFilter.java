package ch.bailu.aat_lib.preferences.location;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

public class SolidDistanceFilter extends SolidIndexList {
    private static final String KEY="distance_filter_";

    private static final float[] VALUE_LIST = {
            0f,
            1f,
            2f,
            4f,
            6f,
            8f,
            10f,
            15f,
            20f,
            25f,
            30f,
            99f,
    };

    private final SolidUnit sunit;

    public SolidDistanceFilter(StorageInterface s, int i) {
        super(s, KEY+i);
        sunit = new SolidUnit(s);
    }


    public float getMinDistance() {
        return VALUE_LIST[getIndex()];
    }


    @Override
    public String getLabel() {
        return Res.str().p_distance_filter();
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }


    @Override
    public String getValueAsString(int i) {
        if (i==0) return Res.str().off();
        if (i==length()-1) Res.str().auto();

        return FF.f().N2.format(VALUE_LIST[i] * sunit.getAltitudeFactor())
                + sunit.getAltitudeUnit();
    }


    @Override
    public String getToolTip() {
        return Res.str().tt_p_distance_filter();
    }

}
