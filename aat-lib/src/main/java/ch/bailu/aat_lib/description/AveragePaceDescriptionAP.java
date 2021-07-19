package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AveragePaceDescriptionAP extends AveragePaceDescription {

    public AveragePaceDescriptionAP(StorageInterface s) {
        super(s);
    }

    @Override
    public String getLabel() {
        return Res.str().pace_ap();
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
