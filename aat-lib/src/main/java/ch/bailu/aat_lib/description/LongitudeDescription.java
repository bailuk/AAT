package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class LongitudeDescription extends DoubleDescription {
    public static final String UNIT="\u00B0";


    @Override
    public String getLabel() {
        return Res.str().d_longitude();
    }

    @Override
    public String getUnit() {
        return UNIT;
    }

    public String getValue() {
        return FF.f().N6.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getLongitude());
    }
}
