package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.util.ToDo;

public class AveragePaceDescription extends PaceDescription {
    public AveragePaceDescription(Context c) {
        super(c);
    }



    @Override
    public String getLabel() {
        return ToDo.translate("Pace");
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(speedToPace(info.getSpeed()));
    }

}
