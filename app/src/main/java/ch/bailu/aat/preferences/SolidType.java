package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;



public abstract class SolidType implements SolidTypeInterface {
    public static final String NULL_LABEL=""; 
    
    
    public String getLabel() {
        return NULL_LABEL;
    }
    
    
    public boolean hasKey(String s) {
        return s.equals(getKey());
    }    
    
    
    public Context getContext() {
        return getStorage().getContext();
    }
    
    
    public void register(OnSharedPreferenceChangeListener listener) {
        getStorage().register(listener);
    }
    
    
    public void unregister(OnSharedPreferenceChangeListener listener) {
        getStorage().unregister(listener);
    }
}
