package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat_lib.description.ContentDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.StepCounterAttributes;
import ch.bailu.aat_lib.resources.Res;

public class TotalStepsDescription extends ContentDescription {

    private String value = VALUE_DISABLED;
    private final String unit;
    private final String label;


    public TotalStepsDescription(Context c) {
        unit = Res.str().sensor_step_total_unit();
        label = Res.str().sensor_step_total();
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
