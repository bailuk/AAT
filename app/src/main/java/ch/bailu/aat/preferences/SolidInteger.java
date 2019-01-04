package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppLog;

public class SolidInteger extends AbsSolidType {
    private final String key;
    private final Storage storage;
    
    
    public SolidInteger(Context c, String k) {
        storage=new Storage(c);
        key=k;
    }

    
    public int getValue() {
        return getStorage().readInteger(getKey());
    }
    
    
    public void setValue(int v) {
        getStorage().writeInteger(getKey(),v);
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

    @Override
    public void setValueFromString(String s) {
        try {
            s = s.trim();
            // only positive/negative Integers, any size allowed
            if (s.matches("-?[1-9]\\d*")) {
                setValue(Integer.valueOf(s));
            } else {
                AppLog.e(getContext(), getString(R.string.error_integer, s));
            }
        } catch (NumberFormatException e) {
            AppLog.e(getContext(), e);
        }
    }
}
