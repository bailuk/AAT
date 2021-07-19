package ch.bailu.aat.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AverageSpeedDescriptionAP extends AverageSpeedDescription {
    public AverageSpeedDescriptionAP(StorageInterface storage) {
        super(storage);
    }

    @Override
    public String getLabel() {
        return Res.str().average_ap();
    }

    @Override
    public String getLabelShort() {
        return super.getLabel();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long apTime = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);
        final long apDistance =
                info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_DISTANCE);

        float distance = info.getDistance() - apDistance;
        long stime = (info.getTimeDelta() - apTime) / 1000;

        float ftime = stime;

        if (ftime > 0f)
            setCache(distance / ftime);

        else
            setCache(0f);
    }
}
