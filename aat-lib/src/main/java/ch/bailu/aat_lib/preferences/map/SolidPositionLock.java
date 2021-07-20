package ch.bailu.aat_lib.preferences.map;


import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Images;
import ch.bailu.aat_lib.resources.Res;

public class SolidPositionLock extends SolidBoolean {
    private static final String POSTFIX = "_POSITION_LOCK";

    public SolidPositionLock(StorageInterface s, String k) {
        super(s, k + POSTFIX);
    }


    @Override
    public int getIconResource() {
        if (getValue()==true) {
            return Images.zoom_original_inverse();

        } else {
            return Images.zoom_original();
        }
    }

    @Override
    public String getValueAsString() {
        return Res.str().tt_map_home();
    }
}
