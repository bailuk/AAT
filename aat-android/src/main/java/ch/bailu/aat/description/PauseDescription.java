package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;


public class PauseDescription extends TimeDescription {

    public PauseDescription(Context context) {
    }

    @Override
    public String getLabel() {
        return Res.str().pause();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getPause());
    }
}
