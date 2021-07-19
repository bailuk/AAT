package ch.bailu.aat.description;

import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes;
import ch.bailu.aat_lib.resources.Res;

public class HeartRateDescription extends ContentDescription {
    public final String LABEL;
    public static final String UNIT = "bpm";

    private String value = VALUE_DISABLED;
    private String label;


    public HeartRateDescription() {
        LABEL = Res.str().sensor_heart_rate();
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
        return UNIT;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        final boolean haveSensor = SensorState.isConnected(InfoID.HEART_RATE_SENSOR);

        if (iid == InfoID.HEART_RATE_SENSOR && haveSensor) {
            String bpm = info.getAttributes().get(HeartRateAttributes.KEY_INDEX_BPM);
            String contact = info.getAttributes().get(HeartRateAttributes.KEY_INDEX_CONTACT);

            value = bpm;
            label = LABEL + " " + contact;

        } else {
            value = VALUE_DISABLED;
            label = LABEL;
        }
    }
}
