package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class AverageSpeedDescription extends SpeedDescription {

    public AverageSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.average);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getSpeed());
    }
}
