package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class EndDateDescription extends DateDescription {

    public EndDateDescription(Context context) {
    }


    @Override
    public String getLabel() {
        return Res.str().d_enddate();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getEndTime());
    }
}
