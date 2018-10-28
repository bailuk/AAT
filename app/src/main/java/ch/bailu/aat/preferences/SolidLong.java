package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.util.ui.AppLog;

public class SolidLong extends AbsSolidType {


    private final String key;
    private final Storage storage;
        
        
    public SolidLong(Context c, String k) {
        storage=new Storage(c);
        key=k;
    }

    public long getValue() {
        return getStorage().readLong(getKey());
    }
        
    public void setValue(long v) {
        getStorage().writeLong(getKey(),v);
    }

    @Override
    public void setValueFromString(String s) {
        try {
            setValue(Long.valueOf(s));
        } catch (NumberFormatException e) {
            AppLog.e(getContext(), e);
        }

    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }
}
