package ch.bailu.aat.preferences.map;

import android.content.Context;

import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidString;
import ch.bailu.aat_lib.preferences.SolidTypeInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.foc.Foc;
import ch.bailu.foc_android.FocAndroid;

public class SolidOverlayFile  implements SolidTypeInterface {
    private static final String KEY_NAME="overlay_path_";
    private static final String KEY_ENABLED="overlay_enabled_";

    private final SolidString path;
    private final SolidBoolean enabled;


    private final Context context;

    public SolidOverlayFile(Context c, int i) {
        path = new SolidString(new Storage(c), KEY_NAME+i);
        enabled = new SolidBoolean(new Storage(c), KEY_ENABLED+i);
        context = c;
    }

    public Context getContext() {
        return context;
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
    public String getKey() {
        return "";
    }


    @Override
    public StorageInterface getStorage() {
        return enabled.getStorage();
    }


    @Override
    public boolean hasKey(String key) {
        return key.contains(KEY_NAME) || key.contains(KEY_ENABLED);
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
