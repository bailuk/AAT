package ch.bailu.aat.description;

import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.resources.Res;

public class TimeApDescription extends TimeDescription {

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);
        setCache(info.getTimeDelta() - autoPause);
    }


    @Override
    public String getLabel() {
        return Res.str().time_ap();
    }

}
