package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.AutoPause;
import ch.bailu.aat_lib.resources.Res;

public class DistanceApDescription extends DistanceDescription {
    public DistanceApDescription(Context context) {
        super(context);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        float apDistance = info.getAttributes().getAsFloat(AutoPause.INDEX_AUTO_PAUSE_DISTANCE);
        setCache(info.getDistance() - apDistance);
    }

    @Override
    public String getLabel() {
        return Res.str().distance_ap();
    }

}
