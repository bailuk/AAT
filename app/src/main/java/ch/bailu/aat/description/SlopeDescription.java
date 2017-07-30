package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;

public class SlopeDescription extends ContentDescription {
    private short slope;

    public SlopeDescription(Context c) {
        super(c);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        slope = info.getSlope();
    }

    @Override
    public String getValue() {
        return String.valueOf(slope);
    }

    public String getUnit() {
        return "%";
    }

    @Override
    public String getLabel() {
        return "Slope*";
    }
}
