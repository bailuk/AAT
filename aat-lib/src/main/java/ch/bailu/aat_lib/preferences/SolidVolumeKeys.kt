package ch.bailu.aat_lib.preferences;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.resources.Res;

public class SolidVolumeKeys extends SolidBoolean {

    private static final String KEY = "USE_VOLUME_KEYS";

    public SolidVolumeKeys (StorageInterface s) {
        super(s, KEY);
    }

    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_use_volume_keys();
    }
}
