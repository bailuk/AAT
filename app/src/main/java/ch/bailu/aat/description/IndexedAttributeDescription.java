package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxListAttributes;

public class IndexedAttributeDescription extends ContentDescription {

    private final String unit;
    private final String label;
    private final int keyIndex;


    public String value = "";

    public IndexedAttributeDescription(Context c, String l, String u, int i) {
        super(c);
        label = l;
        keyIndex = i;
        unit = u;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getUnit() {
        return unit;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        value = info.getAttributes().get(keyIndex);
    }


    public static class Cadence extends IndexedAttributeDescription {

        public Cadence(Context c) {
            super(c, CadenceDescription.LABEL, CadenceDescription.UNIT,
                    GpxListAttributes.INDEX_CADENCE);
        }
    }


    public static class TotalCadence extends IndexedAttributeDescription {

        public TotalCadence(Context c) {
            super(c, "Crank rotations", "Rotations", GpxListAttributes.INDEX_TOTAL_CADENCE);
        }
    }


    public static class HeartRate extends IndexedAttributeDescription {

        public HeartRate(Context c) {
            super(c, HeartRateDescription.LABEL, HeartRateDescription.UNIT, GpxListAttributes.INDEX_AVERAGE_HR);
        }
    }


    public static class HeartBeats extends IndexedAttributeDescription {

        public HeartBeats(Context c) {
            super(c, "Heart Beats", "Beats", GpxListAttributes.INDEX_HEART_BEATS);
        }
    }
}
