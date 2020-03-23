package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.util.ToDo;

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
        return ToDo.translate("Distance (without pauses)");
    }

}
