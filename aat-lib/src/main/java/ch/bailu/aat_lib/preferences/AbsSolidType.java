package ch.bailu.aat_lib.preferences;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.util.Objects;

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
    public final void register(OnPreferencesChanged listener) {
        getStorage().register(listener);
    }


    @Override
    public final void unregister(OnPreferencesChanged listener) {
        getStorage().unregister(listener);
    }


    @Nonnull
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

}
