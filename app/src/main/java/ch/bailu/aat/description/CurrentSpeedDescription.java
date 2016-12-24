package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;
public class CurrentSpeedDescription extends SpeedDescription {

    public CurrentSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.speed);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (setSpeedFromLastPoint(info) == false) {
            setCache(info.getSpeed());
        }
    }

    private boolean setSpeedFromLastPoint(GpxInformation info) {
        final GpxList track = info.getGpxList();

        if (track != null) {
            if (track.getPointList().size() > 0) {
                final GpxDeltaInterface delta = ((GpxDeltaInterface) info.getGpxList().getPointList().getLast());
                if (delta != null) {
                    setCache(delta.getSpeed());
                    return true;
                }
            }

        }
        return false;
    }

}