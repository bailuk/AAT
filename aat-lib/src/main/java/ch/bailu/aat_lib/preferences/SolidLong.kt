package ch.bailu.aat_lib.preferences;

import ch.bailu.aat_lib.exception.ValidationException;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.resources.Res;

public class SolidLong extends AbsSolidType {


    private final String key;
    private final StorageInterface storage;


    public SolidLong(StorageInterface s, String k) {
        storage=s;
        key=k;
    }

    public long getValue() {
        return getStorage().readLong(getKey());
    }

    public void setValue(long v) {
        getStorage().writeLong(getKey(),v);
    }

    @Override
    public void setValueFromString(String s) throws ValidationException {
            s = s.trim();

            if (! validate(s)) {
                throw new ValidationException(String.format(Res.str().error_long(),s));
            } else {
                try {
                    setValue(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    AppLog.e(this, e);
                }
            }
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
    public boolean validate(String s) {
        // regex long, not 100% correct
        return s.matches("^-?\\d{1,19}$");
    }
}
