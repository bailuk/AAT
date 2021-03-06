package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.AutoPause;

public class AveragePaceDescriptionAP extends AveragePaceDescription {

    public AveragePaceDescriptionAP(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.pace_ap);
    }

    @Override
    public String getLabelShort() {
        return super.getLabel();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long apTime = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);
        final float apDistance = info.getAttributes().getAsFloat(AutoPause.INDEX_AUTO_PAUSE_DISTANCE);

        float distance = info.getDistance() - apDistance;
        long stime = (info.getTimeDelta() - apTime) / 1000;

        float ftime = stime;

        if (distance > 0f)
            setCache(ftime / distance);

        else
            setCache(0f);
    }
}
