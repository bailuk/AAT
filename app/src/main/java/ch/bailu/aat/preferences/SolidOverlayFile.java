package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.File;

import ch.bailu.simpleio.foc.Foc;

public class SolidOverlayFile  implements SolidTypeInterface {
    private static final String KEY_NAME="overlay_path_";
    private static final String KEY_ENABLED="overlay_enabled_";

    private final SolidString path;
    private final SolidBoolean enabled;
    private final Storage storage;

    
    public SolidOverlayFile(Context c, int i) {
        storage = Storage.global(c);
        path = new SolidString(storage, KEY_NAME+i);
        enabled = new SolidBoolean(storage, KEY_ENABLED+i);
    }


    public void setPath(Foc file) {
        if (file.exists()) {
            path.setValue(file.toString());
            enabled.setValue(true);
        }
    }


    public File toFile() {
        return new File(getPath());
    }

    public String getName() {
        return toFile().getName();
    }
    public String getPath() {
        return getValueAsString();
    }

    @Override
    public String getLabel() {
        return getName();
    }


    @Override
    public String getValueAsString() {
        return path.getValueAsString();
    }



    public boolean isEnabled() {
        return exists() && enabled.getValue();
    }


    public boolean exists() {
        return toFile().exists();
    }


    public void setEnabled(boolean isChecked) {
        enabled.setValue(isChecked);
    }


    @Override
    public Context getContext() {
        return storage.getContext();
    }


    @Override
    public String getKey() {
        return "";
    }


    @Override
    public Storage getStorage() {
        return storage;
    }


    @Override
    public boolean hasKey(String key) {
        return key.contains(KEY_NAME) || key.contains(KEY_ENABLED);
    }


    @Override
    public void register(OnSharedPreferenceChangeListener listener) {
        storage.register(listener);
        
    }


    @Override
    public void unregister(OnSharedPreferenceChangeListener listener) {
        storage.unregister(listener);
        
    }
}
