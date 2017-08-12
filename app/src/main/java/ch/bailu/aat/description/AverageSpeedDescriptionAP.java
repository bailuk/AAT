package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class AverageSpeedDescriptionAP extends AverageSpeedDescription {
    public AverageSpeedDescriptionAP(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getApLabel(super.getLabel());
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        float distance = info.getDistance();
        long stime = (info.getTimeDelta() - info.getAutoPause())/1000;

        float ftime = stime;

        if (ftime > 0f)
            setCache(distance / ftime);

        else
            setCache(0f);
    }
}
