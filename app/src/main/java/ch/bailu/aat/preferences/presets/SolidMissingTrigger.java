package ch.bailu.aat.preferences.presets;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidStaticIndexList;

public class SolidMissingTrigger extends SolidStaticIndexList {
    private static final String KEY="missing_trigger_";
    
    private static final int[] VALUE_LIST = {
    999,
    10,
    15,
    20,
    25,
    30,
    40,
    50,
    100,
    200,
    };
    

    private static String[] label_list = null;
    private static String[] generateLabelList(Context c) {
        if (label_list == null) {

            label_list = new String[VALUE_LIST.length];

            label_list[0] = c.getString(R.string.off);
            for (int i = 1; i < label_list.length; i++) {
                label_list[i] = String.valueOf(VALUE_LIST[i]) + "s";
            }
        }
        return label_list;
    }

    public SolidMissingTrigger(Context c, int i) {

        super(c, KEY+i, generateLabelList(c));
    }
    
    public int getTriggerSeconds() {
        return VALUE_LIST[getIndex()];
    }
    
    public int getTriggerMillis() {
        return getTriggerSeconds() * 1000;
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_missing_trigger);
    }


    @Override
    public String getToolTip() {
        return getString(R.string.tt_p_missing_trigger);
    }

}
