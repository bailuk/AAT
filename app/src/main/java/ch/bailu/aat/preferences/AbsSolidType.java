package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;



public abstract class AbsSolidType implements SolidTypeInterface {
    public static final String NULL_LABEL="";
    public static final String DEFAULT_MARKER=" ✓";


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



    protected static String toDefaultString(String s) {
        return s + DEFAULT_MARKER;
    }

    protected static String toDefaultString(String s, int sel) {
        return toDefaultString(s, sel, 0);
    }

    protected static String toDefaultString(String s, int sel, int def) {
        if (def == sel) return toDefaultString(s);
        return s;
    }


}
