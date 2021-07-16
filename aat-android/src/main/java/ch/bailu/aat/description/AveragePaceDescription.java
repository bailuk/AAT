package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class AveragePaceDescription extends PaceDescription {
    public AveragePaceDescription(Context c) {
        super(c);
    }



    @Override
    public String getLabel() {
        return getContext().getString(R.string.pace);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(speedToPace(info.getSpeed()));
    }

}
