package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;

import java.util.Locale;

public class SolidAccuracyFilter extends SolidIndexList {
    private static final String KEY="accuracy_filter_";
    
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
    
    public SolidAccuracyFilter(Context c, int i) {
        super(Storage.preset(c), KEY+i);
        sunit = new SolidUnit(c);
    }
    
    public float getMinAccuracy() {
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_accuracy_filter);
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }

    @Override
    public String getValueAsString(int i) {
        if (i==0) return getContext().getString(R.string.off);
        return String.format((Locale)null,"%.3f%s", VALUE_LIST[i] * sunit.getAltitudeFactor(), sunit.getAltitudeUnit());
    }
    



    

}
