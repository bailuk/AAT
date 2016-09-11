package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

import ch.bailu.aat.R;

public class EndDateDescription extends DateDescription {

    public EndDateDescription(Context context) {
        super(context);
    }

    
    @Override
    public String getLabel() {
        return getString(R.string.d_enddate);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getEndTime());
    }
}
