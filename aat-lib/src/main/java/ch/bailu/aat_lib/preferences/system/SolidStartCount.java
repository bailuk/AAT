package ch.bailu.aat_lib.preferences.system;

import ch.bailu.aat_lib.preferences.SolidLong;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidStartCount extends SolidLong {
    private static final String KEY ="start_count";

    public SolidStartCount(StorageInterface s) {
        super(s, KEY);
    }

    public boolean isFirstRun() {
        return getValue() == 0;
    }

    public void increment() {
        setValue(getValue() + 1);
    }
}
