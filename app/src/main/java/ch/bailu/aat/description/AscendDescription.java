package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class AscendDescription extends AltitudeDescription {
    public AscendDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return "Ascend*";
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache( ((float)info.getAscend()) );
    }
}
