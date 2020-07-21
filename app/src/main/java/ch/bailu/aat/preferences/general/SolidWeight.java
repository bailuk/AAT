package ch.bailu.aat.preferences.general;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidInteger;
import ch.bailu.aat.exception.ValidationException;
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
    public void setValueFromString(String s) throws ValidationException {
        s = s.trim();

        if (! validate(s)) {
            throw new ValidationException(getString(R.string.error_integer_positive));
        } else {
            try {
                setValue(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                AppLog.e(getContext(), e);
            }
        }
    }

    @Override
    public boolean validate(String s) {
        // only positive Integers, from 1-999 allowed
        return s.matches("[1-9]\\d{0,2}");
    }

}
