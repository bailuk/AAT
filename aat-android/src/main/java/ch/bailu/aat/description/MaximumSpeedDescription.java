package ch.bailu.aat.description;


import android.content.Context;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.MaxSpeed;
import ch.bailu.aat_lib.resources.Res;

public class MaximumSpeedDescription  extends SpeedDescription {

    public MaximumSpeedDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return Res.str().maximum();
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getAsFloat(MaxSpeed.INDEX_MAX_SPEED));
    }

}

