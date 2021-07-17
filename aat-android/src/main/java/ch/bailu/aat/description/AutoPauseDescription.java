package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.resources.Res;

public class AutoPauseDescription extends TimeDescription {
    public AutoPauseDescription(Context context) {

    }


    @Override
    public String getLabel() {
        return Res.str().p_autopause();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);
        setCache(autoPause);
    }
}

