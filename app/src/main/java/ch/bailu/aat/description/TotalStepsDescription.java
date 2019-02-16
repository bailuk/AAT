package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.services.sensor.attributes.StepCounterAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat.util.ToDo;

public class TotalStepsDescription extends ContentDescription {
    public static final String UNIT = ToDo.translate("Steps");
    public static final String LABEL = ToDo.translate("Total Steps");


    private String value = VALUE_DISABLED;
    private String unit = UNIT;
    private String label = LABEL;


    public TotalStepsDescription(Context c) {
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

        GpxAttributes attr = info.getAttributes();

        if (attr.hasKey(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL)) {
            value = info.getAttributes().get(StepCounterAttributes.KEY_INDEX_STEPS_TOTAL);
        } else {
            value = VALUE_DISABLED;


        }
    }
}
