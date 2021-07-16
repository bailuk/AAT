package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.preferences.general.SolidUnit;


public abstract class SpeedDescription extends FloatDescription{

    private final SolidUnit sunit;

    public SpeedDescription(Context context) {
        super(context);
        sunit = new SolidUnit(context);
    }

    @Override
    public String getUnit() {
        return sunit.getSpeedUnit();
    }


    @Override
    public String getValue() {
        float speed = getCache();
        float speedFactor = sunit.getSpeedFactor();
        speed = speed * speedFactor;
        return FF.f().N1.format(speed);
    }


    public String getSpeedDescription(float value) {
        setCache(value);
        return getValue() + getUnit();
    }
}
