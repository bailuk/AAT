package ch.bailu.aat_lib.preferences.location;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidInteger;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;

public class SolidAltitude extends SolidInteger {

    private final int unit;


    public SolidAltitude(StorageInterface s, String k, int u) {
        super(s, k);
        unit = u;
    }


    public String addUnit(String s) {
        return s + " [" + SolidUnit.ALT_UNIT[unit] + "]";
    }


    @Override
    public String getValueAsString() {
        return String.valueOf(Math.round(getValue() * SolidUnit.ALT_FACTOR[unit]));
    }


    @Override
    public void setValueFromString(String s) {
        try {
            setValue(Math.round(Float.parseFloat(s) / SolidUnit.ALT_FACTOR[unit]));
        } catch (NumberFormatException e) {
            AppLog.e(this, e);
        }
    }


}
