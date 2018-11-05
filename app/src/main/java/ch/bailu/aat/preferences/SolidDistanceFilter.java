package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.description.FF;

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

    public SolidDistanceFilter(Context c, int i) {
        super(c, KEY+i);
        sunit = new SolidUnit(c);
    }
    
    
    public float getMinDistance() {
        return VALUE_LIST[getIndex()];
    }
    
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_distance_filter);
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }


    @Override
    public String getValueAsString(int i) {
        if (i==0) return getContext().getString(R.string.off);
        if (i==length()-1) getContext().getString(R.string.auto);

        return FF.N_2.format(VALUE_LIST[i] * sunit.getAltitudeFactor())
                + sunit.getAltitudeUnit();
    }
    
}
