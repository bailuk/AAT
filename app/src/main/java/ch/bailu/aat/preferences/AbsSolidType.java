package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;



public abstract class AbsSolidType implements SolidTypeInterface {
    public static final String NULL_LABEL="";


    public int getIconResource() {return 0;}

    @Override
    public String getLabel() {
        return NULL_LABEL;
    }

    public abstract void setValueFromString(String s);

    @Override
    public boolean hasKey(String s) {
        return s.equals(getKey());
    }    
    

    @Override
    public Context getContext() {
        return getStorage().getContext();
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
    public String toString() {
        return getValueAsString();
    }
}
