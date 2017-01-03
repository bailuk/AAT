package ch.bailu.aat.preferences;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.description.ContentInterface;
import ch.bailu.aat.util.ContextWrapperInterface;

public interface SolidTypeInterface extends
        ContextWrapperInterface,
        ContentInterface {

    public String getKey();
    public Storage getStorage();
    
    public boolean hasKey(String key);
    
    public void register(OnSharedPreferenceChangeListener listener);
    public void unregister(OnSharedPreferenceChangeListener listener);
}
