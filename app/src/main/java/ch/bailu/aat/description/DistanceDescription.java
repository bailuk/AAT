package ch.bailu.aat.description;

import android.content.Context;

import java.text.DecimalFormat;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidUnit;

public class DistanceDescription extends FloatDescription {
    private final static DecimalFormat FORMAT[] =
            {FF.N_3, FF.N_2, FF.N_1, FF.N};
    private final SolidUnit unit;
    
    public DistanceDescription(Context context) {
        super(context);
        
        unit = new SolidUnit(context);
    }

    
    @Override
    public String getLabel() {
        return getContext().getString(R.string.distance);
    }
    
    
    @Override
    public String getUnit() {
        return unit.getDistanceUnit();
    }

    
    public String getValue() {
        float dist = unit.getDistanceFactor() * getCache();
        
        int format=0;
        for (int x=10; dist >= x && format < FORMAT.length-1; x*=10) format++;
        return FORMAT[format].format(dist);
    }

    


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getDistance());
    }


    public String getDistanceDescription(float distance) {
        float nonSI=distance * unit.getDistanceFactor();
        
        if (nonSI < 1)
            return getAltitudeDescription(distance);

        return FF.N.format(nonSI) + unit.getDistanceUnit();
    }
    
/*
    public String getDistanceDescriptionRounded(float distance) {
        float nonSI=distance * unit.getDistanceFactor();
        
        if (nonSI < 1)
            return getAltitudeDescription(distance);
        
        return FF.N.format(nonSI) + unit.getDistanceUnit();
    }
*/
    
    public String getAltitudeDescription(double value) {
        return FF.N.format(
                value*unit.getAltitudeFactor()) +
                unit.getAltitudeUnit();
    }

}
