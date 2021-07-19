package ch.bailu.aat.preferences.system;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidVolumeKeys extends SolidBoolean {

    private static final String KEY = "USE_VOLUME_KEYS";

    public SolidVolumeKeys (StorageInterface s) {
        super(s, KEY);
    }

    @Override
    public String getLabel() {
        return Res.str().p_use_volume_keys();
    }
}

