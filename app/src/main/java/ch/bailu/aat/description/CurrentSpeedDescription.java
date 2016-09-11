package ch.bailu.aat.description;

import android.content.Context;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;
import ch.bailu.aat.R;
public class CurrentSpeedDescription extends SpeedDescription {

    public CurrentSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.speed);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {

        GpxList track=info.getGpxList();

        if (track != null) {
            GpxDeltaInterface delta = ((GpxDeltaInterface)info.getGpxList().getPointList().getLast());
            if (delta != null) {
                setCache(delta.getSpeed());
            }
        } else {
            setCache(info.getSpeed());  
        }
    }

}
