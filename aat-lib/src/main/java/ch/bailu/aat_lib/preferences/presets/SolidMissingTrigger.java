package ch.bailu.aat_lib.preferences.presets;

import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

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
    private static String[] generateLabelList() {
        if (label_list == null) {

            label_list = new String[VALUE_LIST.length];

            label_list[0] = Res.str().off();
            for (int i = 1; i < label_list.length; i++) {
                label_list[i] = VALUE_LIST[i] + "s";
            }
        }
        return label_list;
    }

    public SolidMissingTrigger(StorageInterface s, int i) {

        super(s, KEY+i, generateLabelList());
    }

    public int getTriggerSeconds() {
        return VALUE_LIST[getIndex()];
    }

    public int getTriggerMillis() {
        return getTriggerSeconds() * 1000;
    }

    @Override
    public String getLabel() {
        return Res.str().p_missing_trigger();
    }


    @Override
    public String getToolTip() {
        return Res.str().tt_p_missing_trigger();
    }

}
