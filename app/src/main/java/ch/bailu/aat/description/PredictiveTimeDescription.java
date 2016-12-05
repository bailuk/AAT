package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.StateID;

public class PredictiveTimeDescription extends TimeDescription {
    private int state = StateID.OFF;
    private long endTime = 0;

    public PredictiveTimeDescription(Context context) {
        super(context);
    }

    @Override
    public String getUnit() {
        return super.getValue();
    }

    @Override
    public String getValue() {
        if (state == StateID.ON && getCache() > 0) {
            return getValue(getCache() + System.currentTimeMillis() - endTime);
        }
        return super.getValue();
    }

    @Override
    public void onContentUpdated(GpxInformation info) {
        super.onContentUpdated(info);

        state = info.getState();
        endTime = info.getEndTime();
    }
}
