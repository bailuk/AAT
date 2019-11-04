package ch.bailu.aat.preferences.map;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.SolidString;
import ch.bailu.aat.preferences.SolidTypeInterface;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.util_java.foc.Foc;

public class SolidOverlayFile  implements SolidTypeInterface {
    private static final String KEY_NAME="overlay_path_";
    private static final String KEY_ENABLED="overlay_enabled_";

    private final SolidString path;
    private final SolidBoolean enabled;


    public SolidOverlayFile(Context c, int i) {
        path = new SolidString(c, KEY_NAME+i);
        enabled = new SolidBoolean(c, KEY_ENABLED+i);
    }


    public void setValueFromFile(Foc file) {
        if (file.exists()) {
            path.setValue(file.getPath());
            enabled.setValue(true);
        }
    }


    public Foc getValueAsFile() {
        return FocAndroid.factory(getContext(), getValueAsString());
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
    public Context getContext() {
        return enabled.getContext();
    }


    @Override
    public String getKey() {
        return "";
    }


    @Override
    public Storage getStorage() {
        return enabled.getStorage();
    }


    @Override
    public boolean hasKey(String key) {
        return key.contains(KEY_NAME) || key.contains(KEY_ENABLED);
    }


    @Override
    public void register(OnSharedPreferenceChangeListener listener) {
        getStorage().register(listener);

    }


    @Override
    public void unregister(OnSharedPreferenceChangeListener listener) {
        getStorage().unregister(listener);

    }

    @Override
    public String getToolTip() {
        return null;
    }
}
