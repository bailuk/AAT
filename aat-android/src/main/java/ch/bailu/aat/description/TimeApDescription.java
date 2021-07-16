package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.gpx.GpxInformation;

public class TimeApDescription extends TimeDescription {

    public TimeApDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE_TIME);
        setCache(info.getTimeDelta() - autoPause);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.time_ap);
    }

}
