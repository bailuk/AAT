package ch.bailu.aat.preferences;

import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.helpers.ContextWrapperInterface;

public interface SolidTypeInterface extends ContextWrapperInterface {
    public String getKey();
    public Storage getStorage();
    
    public String getLabel(); 
    
    public boolean hasKey(String key);
    
    public void register(OnSharedPreferenceChangeListener listener);
    public void unregister(OnSharedPreferenceChangeListener listener);
}
