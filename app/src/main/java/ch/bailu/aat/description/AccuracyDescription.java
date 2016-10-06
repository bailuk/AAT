package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class AccuracyDescription extends AltitudeDescription {
    public AccuracyDescription(Context c) {
        super(c);
    }

    
    @Override
    public String getLabel() {
        return getString(R.string.d_accuracy);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getAccuracy());
    }
}
