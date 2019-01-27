package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxAttributes;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat.util.ToDo;

public class SensorStateDescription extends StateDescription {
    private String unit = "";

    public SensorStateDescription(Context c) {
        super(c);
    }


    @Override
    public String getLabel() {
        return ToDo.translate("Sensors");
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
            unit = info.getAttributes().getValue(SensorList.Attributes.KEY_SENSOR_OVERVIEW);
        }
    }

}
