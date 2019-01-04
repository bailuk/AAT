package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.util.ui.AppLog;

public class SolidWeight extends SolidInteger {

    final private static String KEY="weight";
    
    
    public SolidWeight(Context c) {
        super(c, KEY);
        
    }
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.p_weight_title);
    }

    public void setDefaults() {
        setValue(75);
    }

    @Override
    public void setValueFromString(String s) {
        try {
            s = s.trim();
            // only positive Integers, any size allowed
            if (s.matches("[1-9]\\d*")) {
                setValue(Integer.valueOf(s));
            } else {
                AppLog.e(getContext(), getString(R.string.error_integer_positive, s));
            }
        } catch (NumberFormatException e) {
            AppLog.e(getContext(), e);
        }
    }

}
