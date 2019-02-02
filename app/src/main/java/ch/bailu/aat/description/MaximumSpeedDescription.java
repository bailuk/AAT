package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxListAttributes;
import ch.bailu.aat.gpx.GpxInformation;
public class MaximumSpeedDescription  extends SpeedDescription {

    public MaximumSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.maximum);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getFloatValue(GpxListAttributes.INDEX_MAX_SPEED));
    }

}

