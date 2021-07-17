package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class LatitudeDescription extends LongitudeDescription {


    @Override
    public String getLabel() {
        return Res.str().d_latitude();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getLatitude());
    }

}
