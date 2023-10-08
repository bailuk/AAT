package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidOverlayFileEnabled extends SolidBoolean {
    private static final String KEY_ENABLED="overlay_enabled_";

    public SolidOverlayFileEnabled(StorageInterface storage, int iid) {
        super(storage, KEY_ENABLED + iid);
    }
}
