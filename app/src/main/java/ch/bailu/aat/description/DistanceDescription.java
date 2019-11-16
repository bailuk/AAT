package ch.bailu.aat.description;

import android.content.Context;

import java.text.DecimalFormat;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.general.SolidUnit;

public class DistanceDescription extends FloatDescription {
    private final DecimalFormat[] FORMAT =
            {FF.f().N3, FF.f().N2, FF.f().N1, FF.f().N};
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


        return FF.f().N.format(nonSI) + " " + unit.getDistanceUnit();
    }


    public String getDistanceDescriptionN1(float distance) {
        float nonSI=distance * unit.getDistanceFactor();

        if (nonSI < 1)
            return getAltitudeDescription(distance);


        return FF.f().N1.format(nonSI) + " " + unit.getDistanceUnit();
    }


    public String getAltitudeDescription(double value) {
        return FF.f().N.format(
                value*unit.getAltitudeFactor()) +
                unit.getAltitudeUnit();
    }

}
