package ch.bailu.aat.description;

import java.util.Locale;

import android.content.Context;
import ch.bailu.aat.preferences.SolidUnit;


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
        float speedFactor = (float) sunit.getSpeedFactor(); 
        speed = speed * speedFactor;
        return String.format(Locale.getDefault(),"%03.1f", speed);
        
    }
    
    
    public String getSpeedDescription(float value) {
        setCache(value);
        return String.format(Locale.getDefault(),"%s%s", getValue(), getUnit());
    }
}
