package ch.bailu.aat.description;

import android.content.Context;

import java.text.DecimalFormat;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidUnit;

public class AltitudeDescription extends FloatDescription {

    private final SolidUnit unit;
    
    
    public AltitudeDescription(Context context) {
        super(context);
        unit = new SolidUnit(context);
    }

    
    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.altitude);
    }

    @Override
    public String getUnit() {
        return unit.getAltitudeUnit();
    }

    public String getValue() {
        return getValue(getCache());
    }

    private static final DecimalFormat f0 = new DecimalFormat("0");

    public String getValue(float v) {
        float f = unit.getAltitudeFactor();
        return f0.format(v *f);
    }
    

    public String getValueUnit(float v) {
        return getValue(v) + " " + getUnit();
    }
    
    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache( ((float)info.getAltitude()) );
    }
    


    
}
