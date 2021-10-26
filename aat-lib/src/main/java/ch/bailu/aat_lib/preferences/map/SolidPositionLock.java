package ch.bailu.aat_lib.preferences.map;


import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.resources.ToDo;

public class SolidPositionLock extends SolidBoolean {
    private static final String POSTFIX = "_POSITION_LOCK";

    public SolidPositionLock(StorageInterface s, String k) {
        super(s, k + POSTFIX);
    }


    @Override
    public String getIconResource() {
        if (getValue()==true) {
            return "zoom_original_inverse";

        } else {
            return "zoom_original";
        }
    }

    @Override
    public String getValueAsString() {
        return Res.str().tt_map_home();
    }

    @Override
    public String getToolTip() {
        return Res.str().tt_map_home();
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Center");
    }
}
