package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class TimeApDescription extends TimeDescription {

    public TimeApDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getTimeDelta() - info.getAutoPause());
    }


    @Override
    public String getLabel() {
        return getApLabel(super.getLabel());
    }

}
