package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxTrackAttributes;
import ch.bailu.aat.gpx.GpxInformation;

public class AverageSpeedDescriptionAP extends AverageSpeedDescription {
    public AverageSpeedDescriptionAP(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.average_ap);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getLongValue(GpxTrackAttributes.INDEX_AUTO_PAUSE);
        float distance = info.getDistance();
        long stime = (info.getTimeDelta() - autoPause)/1000;

        float ftime = stime;

        if (ftime > 0f)
            setCache(distance / ftime);

        else
            setCache(0f);
    }
}
