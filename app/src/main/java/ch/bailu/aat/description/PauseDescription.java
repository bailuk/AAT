package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;


public class PauseDescription extends TimeDescription {

    public PauseDescription(Context context) {
        super(context);
    }
    
    @Override
    public String getLabel() {
        return getString(R.string.pause);
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getPause());
    }
}
