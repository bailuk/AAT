package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.DoubleDescription;
import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.description.LongitudeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class BearingDescription extends DoubleDescription {

    @Override
    public String getLabel() {
        return Res.str().d_bearing();
    }

    public String getValue() {
        return FF.f().N3.format(getCache());
    }

    @Override
    public String getUnit() {
        return LongitudeDescription.UNIT;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
//        setCache(info.getBearing());
    }




}
