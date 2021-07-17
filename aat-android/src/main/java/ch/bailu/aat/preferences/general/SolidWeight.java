package ch.bailu.aat.preferences.general;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidInteger;
import ch.bailu.aat_lib.resources.Res;

public class SolidWeight extends SolidInteger {

    final private static String KEY="weight";

    private final Context context;


    public Context getContext() {
        return context;
    }

    public SolidWeight(Context c) {
        super(new Storage(c), KEY);
        context = c;
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
            throw new ValidationException(Res.str().error_integer_positive());
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
