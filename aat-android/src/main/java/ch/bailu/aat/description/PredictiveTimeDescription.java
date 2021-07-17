package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat_lib.description.TimeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.StateID;

public class PredictiveTimeDescription extends TimeDescription {
    private static final int SHOW_LABEL_LIMIT_MS = 5000;

    private long time = 0;
    private long time_paused = 0;

    public PredictiveTimeDescription(Context context) {
    }

    @Override
    public String getUnit() {
        if (time - super.getCache() > SHOW_LABEL_LIMIT_MS)
             return super.getValue();

        return super.getUnit();
    }

    public String getValue() {
        return format(time);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        super.onContentUpdated(iid, info);

        final long endTime = info.getEndTime();
        time = info.getTimeDelta();


        if (info.getState() != StateID.ON) {
            time_paused = time;

        } else if (time_paused != time && endTime > 0) {
            time += (System.currentTimeMillis() - endTime);
        }
    }
}
