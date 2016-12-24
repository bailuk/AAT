package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;

public class PredictiveTimeDescription extends TimeDescription {
    private static final int SHOW_LABEL_LIMIT_MS = 5000;

    private long time = 0;
    private long time_paused = 0;

    public PredictiveTimeDescription(Context context) {
        super(context);
    }

    @Override
    public String getUnit() {
        if (time - super.getCache() > SHOW_LABEL_LIMIT_MS)
             return super.getTime();

        return super.getUnit();
    }

    public String getTime() {
        return getValue(time);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        super.onContentUpdated(iid, info);

        time = info.getTimeDelta();

        if (info.getState() != StateID.ON) {
            time_paused = time;

        } else if (time_paused != time) {
            final long missing = System.currentTimeMillis() - info.getEndTime();

            time += missing;
        }
    }
}
