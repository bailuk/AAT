package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaInterface;
import ch.bailu.aat_lib.resources.Res;

public class CurrentPaceDescription extends PaceDescription {
    public CurrentPaceDescription(Context c) {
        super(c);
    }

    @Override
    public String getLabel() {
        return Res.str().pace();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (setPaceFromLastPoint(info) == false) {
            setCache(info.getSpeed());
        }
    }


    private boolean setPaceFromLastPoint(GpxInformation info) {
        final GpxList track = info.getGpxList();

        if (track != null) {
            if (track.getPointList().size() > 0) {
                final GpxDeltaInterface delta = ((GpxDeltaInterface) info.getGpxList().getPointList().getLast());
                if (delta != null) {
                    setCache(speedToPace(delta.getSpeed()));
                    return true;
                }
            }

        }
        return false;
    }
}