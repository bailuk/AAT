package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidBoolean;

public class SolidVolumeKeys extends SolidBoolean {

    private static final String KEY = "USE_VOLUME_KEYS";

    public SolidVolumeKeys (Context c ) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return getString(R.string.p_use_volume_keys);
    }
}

