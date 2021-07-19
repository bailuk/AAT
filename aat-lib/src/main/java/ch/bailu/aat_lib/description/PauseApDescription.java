package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.resources.Res;

public class PauseApDescription extends PauseDescription {
    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);

        setCache(info.getPause() + autoPause);
    }


    @Override
    public String getLabel() {
        return Res.str().pause_ap();
    }
}
