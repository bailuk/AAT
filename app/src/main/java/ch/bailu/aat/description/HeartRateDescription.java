package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.util.ToDo;

public class HeartRateDescription extends ContentDescription {
    private static final String LABEL = SensorState.getName(InfoID.HEART_RATE_SENSOR);
    private static final String UNIT = "bpm";

    private String value = VALUE_DISABLED;
    private String unit = UNIT;
    private String label = LABEL;


    public HeartRateDescription(Context c) {
        super(c);
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
        final boolean haveSensor = SensorState.isConnected(InfoID.HEART_RATE_SENSOR);

        if (iid == InfoID.HEART_RATE_SENSOR && haveSensor) {
            String bpm = info.getAttributes().getValue(HeartRateAttributes.KEY_INDEX_BPM);

                String bpma = info.getAttributes().getValue(HeartRateAttributes.KEY_INDEX_BPM_AVERAGE);
                String contact = info.getAttributes().getValue(HeartRateAttributes.KEY_INDEX_CONTACT);

                value = bpma;
                label = LABEL + " " + contact;
                unit = bpm + " " + UNIT;

         } else {
            value = VALUE_DISABLED;
            label = LABEL;
            unit = UNIT;
        }
    }
}
