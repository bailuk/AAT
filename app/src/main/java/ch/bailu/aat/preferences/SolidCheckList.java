package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;


public abstract class SolidCheckList extends SolidType {

    public abstract CharSequence[] getStringArray();
    public abstract boolean[] getEnabledArray();
    public abstract void setEnabled(int i, boolean isChecked);
    
    
 

    @Override
    public void setValueFromString(String s) {}

    
    public void register(OnSharedPreferenceChangeListener listener) {
        getStorage().register(listener);
        
    }


    public void unregister(OnSharedPreferenceChangeListener listener) {
        getStorage().unregister(listener);
        
    }



    
    public Context getContext() {
        return getStorage().getContext();
    }

    @Override
    public String getValueAsString() {
        return null;
    }

}

