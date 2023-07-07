package ch.bailu.aat_lib.description;

import java.text.DecimalFormat;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.general.SolidUnit;
import ch.bailu.aat_lib.resources.Res;

public class AltitudeDescription extends FloatDescription {

    private final SolidUnit unit;


    public AltitudeDescription(StorageInterface storageInterface) {
        unit = new SolidUnit(storageInterface);
    }



    @Override
    public String getLabel() {
        return Res.str().altitude();
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
