package ch.bailu.aat_lib.description;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.sensor.SensorState;

public class StepRateDescription extends ContentDescription {
    public static final String UNIT = "spm";

    private String value = VALUE_DISABLED;
    private String unit = UNIT;
    private final String label;

    public StepRateDescription() {
        label = Res.str().sensor_step_counter();
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
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        final boolean haveSensor = SensorState.isConnected(InfoID.STEP_COUNTER_SENSOR);

        if (iid == InfoID.STEP_COUNTER_SENSOR && haveSensor) {
            value = info.getAttributes().get(StepCounterAttributes.KEY_INDEX_STEPS_RATE);
            unit = UNIT + " (" + info.getAttributes().get(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL) + ")";

        } else {
            value = VALUE_DISABLED;
        }
    }
}
