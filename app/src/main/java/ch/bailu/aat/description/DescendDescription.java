package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxTrackAttributes;
import ch.bailu.aat.gpx.GpxInformation;

public class DescendDescription extends AltitudeDescription {
    public DescendDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_descend);
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getFloatValue(GpxTrackAttributes.INDEX_DESCEND));
    }
}
