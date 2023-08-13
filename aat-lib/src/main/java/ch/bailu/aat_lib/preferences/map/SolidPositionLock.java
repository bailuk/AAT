package ch.bailu.aat_lib.preferences.map;


import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidPositionLock extends SolidBoolean {
    private static final String POSTFIX = "_POSITION_LOCK";

    public SolidPositionLock(StorageInterface s, String k) {
        super(s, k + POSTFIX);
    }

    public void setDefaults() {
        setValue(true);
    }

    @Override
    public String getIconResource() {
        if (getValue()) {
            return "zoom_original_inverse";
        } else {
            return "zoom_original";
        }
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        return Res.str().tt_map_home();
    }

    @Override
    public String getToolTip() {
        return Res.str().tt_map_home();
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().location_title();
    }
}
