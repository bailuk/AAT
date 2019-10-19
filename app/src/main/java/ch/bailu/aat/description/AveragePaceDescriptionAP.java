package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.AutoPause;
import ch.bailu.aat.util.ToDo;

public class AveragePaceDescriptionAP extends AveragePaceDescription {

    public AveragePaceDescriptionAP(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return ToDo.translate("Pace (without pauses)");
    }

    @Override
    public String getLabelShort() {
        return super.getLabel();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final long autoPause = info.getAttributes().getAsLong(AutoPause.INDEX_AUTO_PAUSE);
        float distance = info.getDistance();
        long stime = (info.getTimeDelta() - autoPause) / 1000;

        float ftime = stime;

        if (distance > 0f)
            setCache(ftime / distance);

        else
            setCache(0f);
    }
}
