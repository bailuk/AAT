package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.preferences.SolidIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.ToDo;

public class SolidScaleFactor extends SolidIndexList {
    private static final String KEY = "map_scale_factor";

    private static final float[] VALUE_LIST = {
            1.0f,
            1.2f,
            1.4f,
            1.6f,
            1.8f,
            2.0f,
            2.2f,
            2.4f,
            2.6f,
            0.6f,
            0.8f,
    };

    public SolidScaleFactor(StorageInterface storage) {
        super(storage, KEY);
    }

    public float getScaleFactor() {
        return VALUE_LIST[getIndex()];
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Scale factor");
    }

    @Override
    public int length() {
        return VALUE_LIST.length;
    }

    @Override
    public String getValueAsString(int index) {
        int i = validate(index);
        if (i == 0) return toDefaultString(String.valueOf(VALUE_LIST[i]));
        return String.valueOf(VALUE_LIST[i]);
    }
}
