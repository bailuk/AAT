package ch.bailu.aat_lib.preferences.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidAdjustGpsAltitude extends SolidBoolean {
    private final static String KEY = "UseGpsAltitudeCorrection";

    public SolidAdjustGpsAltitude(StorageInterface s) {
        super(s, KEY);
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_adjust_altitude();
    }
}
