package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.AutoPause;

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
        return getContext().getString(R.string.distance_ap);
    }

}
