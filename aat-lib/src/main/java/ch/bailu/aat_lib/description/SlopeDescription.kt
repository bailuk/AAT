package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AltitudeDelta;
import ch.bailu.aat_lib.resources.Res;

public class SlopeDescription extends ContentDescription {
    private String slope="0";


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        slope = info.getAttributes().get(AltitudeDelta.INDEX_SLOPE);
    }

    @Override
    public String getValue() {
        return slope;
    }

    public String getUnit() {
        return "%";
    }

    @Override
    public String getLabel() {
        return Res.str().d_slope();
    }
}
