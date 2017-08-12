package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class PauseApDescription extends PauseDescription {
    public PauseApDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getPause() + info.getAutoPause());
    }


    @Override
    public String getLabel() {
        return getApLabel(super.getLabel());
    }
}
