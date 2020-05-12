package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.attributes.GpxAttributes;
import ch.bailu.aat.services.sensor.list.SensorList;

public class SensorStateDescription extends StateDescription {
    private String unit = "";

    public SensorStateDescription(Context c) {
        super(c);
    }


    @Override
    public String getLabel() {
        return getContext().getString(R.string.sensor_state);
    }


    @Override
    public String getUnit() {
        return unit;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        super.onContentUpdated(iid, info);

        GpxAttributes attributes = info.getAttributes();

        if (attributes != null) {
            unit = info.getAttributes().get(SensorList.Attributes.KEY_SENSOR_OVERVIEW);
        }
    }

}
