package ch.bailu.aat.preferences;

import ch.bailu.aat.R;
import android.content.Context;

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
    
    private static final String[] LABEL_LIST = {
    "off*",
    "10s",
    "15s",
    "20s",
    "25s",
    "30s",
    "40s",
    "50s",
    "100s",
    "200s",
    };

    public SolidMissingTrigger(Context c, int i) {
        super(Storage.preset(c), KEY+i, LABEL_LIST);
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

}
