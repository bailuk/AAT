package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.SolidTypeInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocFactory;

public class SolidOverlayFile  implements SolidTypeInterface {
    private static final String KEY_NAME="overlay_path_";

    private final SolidString path;
    private final SolidOverlayFileEnabled enabled;

    private final FocFactory focFactory;

    public SolidOverlayFile(StorageInterface storage, FocFactory focFactory, int iid) {
        validateOverlayIID(iid);
        path = new SolidString(storage, KEY_NAME + iid);
        enabled = new SolidOverlayFileEnabled(storage, iid);
        this.focFactory = focFactory;
    }

    private void validateOverlayIID(int i) {
        if (! (i >= InfoID.OVERLAY && i < InfoID.OVERLAY + SolidOverlayFileList.MAX_OVERLAYS)) {
            AppLog.e(this, "Invalid overlay ID: " + i);
        }
    }

    public void setValueFromFile(Foc file) {
        if (file.exists()) {
            path.setValue(file.getPath());
            enabled.setValue(true);
        }
    }

    public Foc getValueAsFile() {
        return focFactory.toFoc(getValueAsString());
    }

    @Override
    public String getLabel() {
        return getValueAsFile().getName();
    }

    @Override
    public String getValueAsString() {
        return path.getValueAsString();
    }

    public boolean isEnabled() {
        return getValueAsFile().exists() && enabled.getValue();
    }

    public void setEnabled(boolean isChecked) {
        enabled.setValue(isChecked);
    }

    @Override
    public String getKey() {
        return path.getKey();
    }

    @Override
    public StorageInterface getStorage() {
        return enabled.getStorage();
    }

    @Override
    public boolean hasKey(String key) {
        return key.contains(KEY_NAME) || enabled.hasKey(key);
    }

    @Override
    public void register(OnPreferencesChanged listener) {
        getStorage().register(listener);
    }

    @Override
    public void unregister(OnPreferencesChanged listener) {
        getStorage().unregister(listener);
    }

    @Override
    public String getToolTip() {
        return null;
    }
}
