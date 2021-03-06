package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.StepCounterAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

public class StepRateDescription extends ContentDescription {
    public static final String UNIT = "spm";



    private String value = VALUE_DISABLED;
    private String unit = UNIT;
    private final String label;


    public StepRateDescription(Context c) {
        super(c);
        label = c.getString(R.string.sensor_step_counter);
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

        final boolean haveSensor = SensorState.isConnected(InfoID.STEP_COUNTER_SENSOR);

        if (iid == InfoID.STEP_COUNTER_SENSOR && haveSensor) {

            value = info.getAttributes().get(StepCounterAttributes.KEY_INDEX_STEPS_RATE);
            unit = UNIT + " (" + info.getAttributes().get(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL) + ")";

        } else {
            value = VALUE_DISABLED;

        }
    }
}
