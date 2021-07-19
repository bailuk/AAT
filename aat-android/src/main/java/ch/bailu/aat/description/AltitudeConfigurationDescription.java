package ch.bailu.aat.description;

import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.services.sensor.list.SensorState;
import ch.bailu.aat_lib.description.AltitudeDescription;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;

public class AltitudeConfigurationDescription extends AltitudeDescription {

    private String configuration;

    private boolean haveSensor = SensorState.isConnected(InfoID.BAROMETER_SENSOR);

    private final SolidAdjustGpsAltitude sadjustAltitude;

    public AltitudeConfigurationDescription(StorageInterface storage) {
        super(storage);
        sadjustAltitude = new SolidAdjustGpsAltitude(storage);
        setLabel();

    }

    private void setLabel() {
        if (haveSensor) {
            configuration  = " " + Res.str().sensor_barometer();

        } else if (sadjustAltitude.isEnabled()) {
            configuration = " GPS+-";

        } else {
            configuration = " GPS";
        }

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation information) {
        final boolean state = SensorState.isConnected(InfoID.BAROMETER_SENSOR);

        if (haveSensor != state) {
            haveSensor = state;
            setLabel();
        }

        super.onContentUpdated(iid, information);
    }


    @Override
    public String getLabel() {
        return super.getLabel() + configuration;
    }
}
