package ch.bailu.aat.preferences;

import android.content.Context;
import ch.bailu.aat.exception.ValidationException;

import java.util.ArrayList;

public class SolidString extends AbsSolidType {
    private final String key;
    private final Storage storage;


    public SolidString(Context c, String k) {
        storage=new Storage(c);
        key=k;
    }

    @Override
    public String getValueAsString() {
        return getStorage().readString(getKey());
    }

    @Override
    public void setValueFromString(String s) throws ValidationException {
        setValue(s);
    }

    public void setValue(String v) {
        getStorage().writeString(key, v);
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }


    public ArrayList<String> buildSelection(ArrayList<String> strings) {
        return strings;
    }

    public String getValueAsStringNonDef() {
        String s = getValueAsString();
        if (s== null || s.equals(Storage.DEF_VALUE)) return "";
        return s;
    }
}
