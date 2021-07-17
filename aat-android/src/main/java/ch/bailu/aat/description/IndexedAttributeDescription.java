package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.SampleRate;
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes;

public class IndexedAttributeDescription extends ContentDescription {

    private final String unit;
    private final String label;
    private final int keyIndex;


    public String value = "";

    public IndexedAttributeDescription(Context c, String l, String u, int i) {
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
            super(c, c.getString(R.string.sensor_cadence), CadenceDescription.UNIT,
                    SampleRate.Cadence.INDEX_CADENCE);
        }
    }


    public static class TotalCadence extends IndexedAttributeDescription {

        public TotalCadence(Context c) {
            super(c,c.getString(R.string.sensor_cadence_total),
                    c.getString(R.string.sensor_cadence_total_unit),
                    SampleRate.Cadence.INDEX_TOTAL_CADENCE);
        }
    }


    public static class HeartRate extends IndexedAttributeDescription {

        public HeartRate(Context c) {
            super(c, c.getString(R.string.sensor_heart_rate), HeartRateDescription.UNIT, SampleRate.HeartRate.INDEX_AVERAGE_HR);
        }
    }


    public static class HeartBeats extends IndexedAttributeDescription {

        public HeartBeats(Context c) {
            super(c,c.getString(R.string.sensor_heart_beats),
                    c.getString(R.string.sensor_cadence_total_unit),
                    SampleRate.HeartRate.INDEX_HEART_BEATS);
        }
    }


    public static class StepsRate extends IndexedAttributeDescription {
        public StepsRate(Context c) {
            super(c,c.getString(R.string.sensor_step_rate),
                    c.getString(R.string.sensor_step_rate_unit),
                    StepCounterAttributes.KEY_INDEX_STEPS_RATE);
        }
    }

    public static class StepsTotal extends IndexedAttributeDescription {
        public StepsTotal(Context c) {
            super(c,c.getString(R.string.sensor_step_total),
                    c.getString(R.string.sensor_step_total_unit),
                    StepCounterAttributes.KEY_INDEX_STEPS_TOTAL);
        }
    }
}
