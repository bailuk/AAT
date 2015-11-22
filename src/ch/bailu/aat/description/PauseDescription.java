package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.R;


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
