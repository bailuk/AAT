package ch.bailu.aat.description;

import ch.bailu.aat.services.sensor.list.SensorList;
import ch.bailu.aat_lib.description.StateDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.resources.Res;

public class SensorStateDescription extends StateDescription {
    private String unit = "";

    @Override
    public String getLabel() {
        return Res.str().sensors();
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
