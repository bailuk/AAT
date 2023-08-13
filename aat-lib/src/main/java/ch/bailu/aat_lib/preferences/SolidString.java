package ch.bailu.aat_lib.preferences;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.exception.ValidationException;

public class SolidString extends AbsSolidType {
    private final String key;
    private final StorageInterface storage;


    public SolidString(StorageInterface storage, String key) {
        this.storage = storage;
        this.key = key;
    }

    @Nonnull
    @Override
    public String getValueAsString() {
        return getStorage().readString(getKey());
    }

    @Override
    public void setValueFromString(String string) throws ValidationException {
        setValue(string);
    }

    public void setValue(String v) {
        getStorage().writeString(key, v);
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public StorageInterface getStorage() {
        return storage;
    }


    public ArrayList<String> buildSelection(ArrayList<String> strings) {
        return strings;
    }

    public String getValueAsStringNonDef() {
        String s = getValueAsString();
        if (storage.isDefaultString(s)) return "";
        return s;
    }
}
