package ch.bailu.aat.preferences.system;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.util.ToDo;

public class SolidVolumeKeys extends SolidBoolean {

    private static final String KEY = "USE_VOLUME_KEYS";

    public SolidVolumeKeys (Context c ) {
        super(c, KEY);
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Use volume buttons for map zoom.");
    }
}

