package ch.bailu.aat.description;

import java.util.Locale;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidUnit;

import ch.bailu.aat.R;

public class AltitudeDescription extends FloatDescription {

    private final SolidUnit unit;
    
    
    public AltitudeDescription(Context context) {
        super(context);
        unit = new SolidUnit(context);
    }

    
    
    @Override
    public String getLabel() {
        return getString(R.string.altitude);
    }

    @Override
    public String getUnit() {
        return unit.getAltitudeUnit();
    }

    @Override
    public String getValue() {
        return getValue(getCache());
    }

    
    public String getValue(float v) {
        float f = unit.getAltitudeFactor();
        return String.format((Locale)null, "%.0f", v * f);
    }
    

    public String getValueUnit(float v) {
        float f = unit.getAltitudeFactor();
        return String.format((Locale)null, "%.0f %s", v * f, getUnit());
        
    }
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache( ((float)info.getAltitude()) );
    }
    
    
    public static String getValuePercentage(float height, float distance) {
        
        if (distance != 0f)
            return String.format((Locale)null, "%d %%", Math.round(height* 100 / distance));
        return "";
    }
    
}
