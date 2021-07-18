package ch.bailu.aat.preferences.map;


import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.resources.Res;

public class SolidPositionLock extends SolidBoolean {
    private static final String POSTFIX = "_POSITION_LOCK";

    public SolidPositionLock(Storage s, String k) {
        super(s, k + POSTFIX);
    }


    @Override
    public int getIconResource() {
        if (getValue()==true) {
            return R.drawable.zoom_original_inverse;

        } else {
            return R.drawable.zoom_original;
        }
    }

    @Override
    public String getValueAsString() {
        return Res.str().tt_map_home();
    }
}
