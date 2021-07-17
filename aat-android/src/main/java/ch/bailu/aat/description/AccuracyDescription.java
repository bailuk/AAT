package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class AccuracyDescription extends AltitudeDescription {
    public AccuracyDescription(Context c) {
        super(c);
    }


    @Override
    public String getLabel() {
        return Res.str().d_accuracy();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAccuracy());
    }
}
