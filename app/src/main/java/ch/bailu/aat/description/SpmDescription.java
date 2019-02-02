package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxListAttributes;

public class SpmDescription extends ContentDescription {

    public static final String UNIT_BPM = "bpm";
    public static final String UNIT_RPM = "rpm";

    private final String UNIT;
    private final String LABEL;
    private final int INDEX;


    public String value = "";

    public SpmDescription(Context c, String label, String unit, int index) {
        super(c);
        LABEL = label;
        INDEX = index;
        UNIT = unit;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return LABEL;
    }


    @Override
    public String getUnit() {
        return UNIT;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        value = info.getAttributes().getValue(INDEX);
    }


    public static class Cadence extends SpmDescription {

        public Cadence(Context c) {
            super(c, "Cadence", UNIT_RPM, GpxListAttributes.INDEX_CADENCE);
        }
    }


    public static class TotalCadence extends SpmDescription {

        public TotalCadence(Context c) {
            super(c, "Crank rotations", "Rotations", GpxListAttributes.INDEX_TOTAL_CADENCE);
        }
    }


    public static class HR extends SpmDescription {

        public HR(Context c) {
            super(c, "Heart Rate", UNIT_BPM, GpxListAttributes.INDEX_AVERAGE_HR);
        }
    }


    public static class HeartBeats extends SpmDescription {

        public HeartBeats(Context c) {
            super(c, "Heart Beats", "Beats", GpxListAttributes.INDEX_HEART_BEATS);
        }
    }

}
