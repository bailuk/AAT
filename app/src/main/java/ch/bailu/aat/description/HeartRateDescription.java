package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.HeartRateAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

public class HeartRateDescription extends ContentDescription {
    public final String LABEL;
    public static final String UNIT = "bpm";

    private String value = VALUE_DISABLED;
    private String unit = UNIT;
    private String label;


    public HeartRateDescription(Context c) {
        super(c);
        LABEL = c.getString(R.string.sensor_heart_rate);
        label = LABEL;
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
            String bpm = info.getAttributes().get(HeartRateAttributes.KEY_INDEX_BPM);
            String contact = info.getAttributes().get(HeartRateAttributes.KEY_INDEX_CONTACT);

            value = bpm;
            label = LABEL + " " + contact;
            unit = UNIT;

        } else {
            value = VALUE_DISABLED;
            label = LABEL;
            unit = UNIT;
        }
    }
}
