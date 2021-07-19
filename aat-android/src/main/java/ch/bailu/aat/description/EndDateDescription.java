package ch.bailu.aat.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class EndDateDescription extends DateDescription {

    @Override
    public String getLabel() {
        return Res.str().d_enddate();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getEndTime());
    }
}
