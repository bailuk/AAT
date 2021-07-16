package ch.bailu.aat.preferences;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import androidx.annotation.NonNull;

import ch.bailu.aat.exception.ValidationException;
import ch.bailu.util.Objects;


public abstract class AbsSolidType implements SolidTypeInterface {
    public static final String NULL_LABEL="";
    public static final String DEFAULT_MARKER=" ✓";


    public int getIconResource() {return 0;}

    @Override
    public String getLabel() {
        return NULL_LABEL;
    }

    public abstract void setValueFromString(String s) throws ValidationException;

    @Override
    public boolean hasKey(String s) {
        return Objects.equals(s, getKey());
    }


    @Override
    public final Context getContext() {
        return getStorage().getContext();
    }


    @Override
    public final void register(OnSharedPreferenceChangeListener listener) {
        getStorage().register(listener);
    }


    @Override
    public final void unregister(OnSharedPreferenceChangeListener listener) {
        getStorage().unregister(listener);
    }


    @NonNull
    @Override
    public String toString() {
        return getValueAsString();
    }

    @Override
    public String getToolTip() {
        return null;
    }

    protected static String toDefaultString(String s) {
        return s + DEFAULT_MARKER;
    }
    protected static String toDefaultString(String s, int sel) {
        if (sel == 0) return toDefaultString(s);
        return s;
    }

    public boolean validate(String s) { return true; }

    public final String getString(int res) {
        return getContext().getString(res);
    }

    public final String getString(int res, Object obj) {
        return getContext().getString(res, obj.toString());
    }
}
