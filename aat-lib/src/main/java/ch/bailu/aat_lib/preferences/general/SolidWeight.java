package ch.bailu.aat_lib.preferences.general;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidInteger;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidWeight extends SolidInteger {

    final private static String KEY="weight";

    public SolidWeight(StorageInterface s) {
        super(s, KEY);
    }

    @Override
    public String getLabel() {
        return Res.str().p_weight_title();
    }

    public void setDefaults() {
        setValue(75);
    }

    @Override
    public void setValueFromString(String s) throws ValidationException {
        s = s.trim();

        if (! validate(s)) {
            throw new ValidationException(Res.str().error_integer_positive());
        } else {
            try {
                setValue(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                AppLog.e(this, e);
            }
        }
    }

    @Override
    public boolean validate(String s) {
        // only positive Integers, from 1-999 allowed
        return s.matches("[1-9]\\d{0,2}");
    }

}
