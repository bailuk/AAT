package ch.bailu.aat.preferences;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.description.ContentInterface;
import ch.bailu.aat.util.ContextWrapperInterface;
import ch.bailu.aat.util.ui.ToolTipProvider;

public interface SolidTypeInterface extends
        ContextWrapperInterface,
        ContentInterface,
        ToolTipProvider {

    String getKey();
    Storage getStorage();

    boolean hasKey(String key);

    void register(OnSharedPreferenceChangeListener listener);
    void unregister(OnSharedPreferenceChangeListener listener);
}
