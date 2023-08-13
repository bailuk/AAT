package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public abstract class SolidOverlay implements SolidOverlayInterface {
    private final SolidOverlayFileEnabled enabled;
    private final SolidDataDirectory baseDirectory;
    private final String subDir;
    private final String fileName;
    private final int iid;

    public SolidOverlay(SolidDataDirectory baseDirectory, int iid, String subDir, String fileName) {
        this.iid = iid;
        this.fileName = fileName;
        this.subDir = subDir;
        this.enabled = new SolidOverlayFileEnabled(baseDirectory.getStorage(), iid);
        this.baseDirectory = baseDirectory;
    }

    @Override
    public String getLabel() {
        return Res.str().p_mapsforge_poi();
    }

    @Override
    public Foc getValueAsFile() {
        return getDirectory().child(fileName);
    }

    @Override
    public String getValueAsString() {
        return getValueAsFile().toString();
    }

    public Foc getDirectory() {
        return AppDirectory.getDataDirectory(baseDirectory, subDir);
    }

    @Override
    public String getKey() {
        return enabled.getKey();
    }

    @Override
    public StorageInterface getStorage() {
        return enabled.getStorage();
    }

    @Override
    public boolean hasKey(String key) {
        return enabled.hasKey(key) || baseDirectory.hasKey(key);
    }

    @Override
    public void register(OnPreferencesChanged listener) {
        enabled.register(listener);
    }

    @Override
    public void unregister(OnPreferencesChanged listener) {
        enabled.unregister(listener);
    }

    @Override
    public boolean isEnabled() {
        return enabled.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled.setValue(enabled);
    }

    @Override
    public String getToolTip() {
        return null;
    }

    @Override
    public int getIID() {
        return iid;
    }
}
