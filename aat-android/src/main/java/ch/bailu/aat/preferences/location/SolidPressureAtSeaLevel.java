package ch.bailu.aat.preferences.location;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.preferences.SolidInteger;

public class SolidPressureAtSeaLevel extends SolidInteger {
    private final static String KEY = "PressureAtSeaLevel";
    public SolidPressureAtSeaLevel(Context c) {
        super(c, KEY);
    }


    @Override
    public String getLabel() {
        return getString(R.string.p_pressure_sealevel);
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

        }
    }


    @Override
    public String getValueAsString() {
        return String.valueOf(getPressure());
    }

}
