package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.attributes.AltitudeDelta;
import ch.bailu.aat.gpx.GpxInformation;

public class AscendDescription extends AltitudeDescription {
    public AscendDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_ascend);
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAttributes().getAsFloat(AltitudeDelta.INDEX_ASCEND));
    }
}
