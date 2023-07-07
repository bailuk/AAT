package ch.bailu.aat_lib.description;


import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class DateDescription extends LongDescription {
    @Override
    public String getLabel() {
        return Res.str().d_startdate();
    }

    @Override
    public String getValue()   {
        return FF.f().LOCAL_DATE_TIME.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getStartTime());
    }

}
