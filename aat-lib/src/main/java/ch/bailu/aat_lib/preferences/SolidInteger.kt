package ch.bailu.aat_lib.preferences;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;

public class SolidInteger extends AbsSolidType {
    private final String key;
    private final StorageInterface storage;


    public SolidInteger(@Nonnull StorageInterface storage, @Nonnull String key) {
        this.storage = storage;
        this.key = key;
    }


    public int getValue() {
        return getStorage().readInteger(getKey());
    }


    public void setValue(int v) {
        getStorage().writeInteger(getKey(), v);
    }


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public StorageInterface getStorage() {
        return storage;
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getValue());
    }


    @Override
    public void setValueFromString(String s) throws ValidationException {
        s = s.trim();

        if (!validate(s)) {
            throw new ValidationException(String.format(Res.str().error_integer(),s));
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
        // only positive/negative Integers, any size allowed
        return s.matches("-?[1-9]\\d*");
    }
}
