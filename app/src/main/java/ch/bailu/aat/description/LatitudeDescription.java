package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
public class LatitudeDescription extends LongitudeDescription {

    public LatitudeDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.d_latitude);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getLatitude());
    }

}
