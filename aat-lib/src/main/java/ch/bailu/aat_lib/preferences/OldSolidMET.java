package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.preferences.general.SolidPresetCount;
import ch.bailu.aat_lib.resources.Res;

public class OldSolidMET extends SolidStaticIndexList {
    private final static String KEY="met";



    public OldSolidMET(StorageInterface s, int i) {
        super(s, KEY+i, Res.str().p_met_list());

    }


    public static void setDefaults(StorageInterface s) {
        for (int i = 0; i < SolidPresetCount.DEFAULT; i++) {
            new OldSolidMET(s, i).setIndex(i);
        }
    }

    @Override
    public String getLabel() {
        return Res.str().p_met();
    }
}
