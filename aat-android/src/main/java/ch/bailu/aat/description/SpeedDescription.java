package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.description.FloatDescription;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;


public abstract class SpeedDescription extends FloatDescription {

    private final SolidUnit sunit;

    public SpeedDescription(StorageInterface storage) {
        sunit = new SolidUnit(storage);
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
