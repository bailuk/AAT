package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class AverageSpeedDescription extends SpeedDescription {

    public AverageSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return Res.str().average();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getSpeed());
    }
}
