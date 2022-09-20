package ch.bailu.aat_lib.preferences.location;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.SolidInteger;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class SolidPressureAtSeaLevel extends SolidInteger {
    private final static String KEY = "PressureAtSeaLevel";
    public SolidPressureAtSeaLevel(StorageInterface storage) {
        super(storage, KEY);
    }

    @Override
    public String getLabel() {
        return Res.str().p_pressure_sealevel();
    }

    public void setPressure(float pressure) {
        setValue((int) (pressure * 100f));
    }

    public float getPressure() {
        return getValue() / 100f;
    }

    @Override
    public void setValueFromString(String s) {
        try {
            setPressure(Float.parseFloat(s));
        } catch (NumberFormatException e) {
            AppLog.e(this, e);
        }
    }

    @Override
    public String getValueAsString() {
        return String.valueOf(getPressure());
    }
}
