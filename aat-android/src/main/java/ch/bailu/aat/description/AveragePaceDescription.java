package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class AveragePaceDescription extends PaceDescription {
    public AveragePaceDescription(Context c) {
        super(c);
    }



    @Override
    public String getLabel() {
        return Res.str().pace();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(speedToPace(info.getSpeed()));
    }

}
