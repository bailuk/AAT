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

    private static String[] label_list = null;
    private static String[] generateLabelList(Context c) {
        if (label_list == null) {

            label_list = new String[VALUE_LIST.length];

            label_list[0] = c.getString(R.string.off);
            for (int i = 1; i < label_list.length; i++) {
                label_list[i] = String.valueOf(VALUE_LIST[i]) + "m/s²";
            }
        }
        return label_list;
    }

    public SolidAccelerationFilter(Context c, int i) {
        super(Storage.preset(c), KEY+i, generateLabelList(c));
    }




    public float getMaxAcceleration() {
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_acceleration_filter);
    }
}
