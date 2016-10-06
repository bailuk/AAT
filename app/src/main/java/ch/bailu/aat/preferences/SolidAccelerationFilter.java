package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;

public class SolidAccelerationFilter extends SolidStaticIndexList {
    private static final String KEY="accel_filter";
    
    private static final float[] VALUE_LIST = {
    99999f,
    0.25f,
    0.50f,
    0.75f,
    1.00f,
    1.25f,
    1.50f,
    1.75f,
    2.00f,
    2.25f,
    2.50f,
    2.75f,
    3.00f,
    };
    
   private static final String[] LABEL_LIST = {
    "off*",
    "0.25m/s\u00B2",
    "0.50m/s\u00B2",
    "0.75m/s\u00B2",
    "1.00m/s\u00B2",
    "1.25m/s\u00B2",
    "1.50m/s\u00B2",
    "1.75m/s\u00B2",
    "2.00m/s\u00B2",
    "2.25m/s\u00B2",
    "2.50m/s\u00B2",
    "2.75m/s\u00B2",
    "3.00m/s\u00B2",
    };

    public SolidAccelerationFilter(Context c, int i) {
        super(Storage.preset(c), KEY+i, LABEL_LIST);
    }
    
    public float getMaxAcceleration() {
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_acceleration_filter);
    }
}
