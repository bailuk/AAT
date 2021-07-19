package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class AccelerationDescription extends FloatDescription {

    private final static String UNIT="m/s\u00B2";


    @Override
    public String getLabel() {
        return Res.str().d_acceleration();
    }

    public String getUnit() {
        return UNIT;
    }


    public String getValue() {
        return FF.f().N2.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAcceleration());
    }
}
