package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.preferences.location.SolidAdjustGpsAltitude;
import ch.bailu.aat.services.sensor.list.SensorState;

public class AltitudeConfigurationDescription extends AltitudeDescription {

    private String configuration;

    private boolean haveSensor = SensorState.isConnected(InfoID.BAROMETER_SENSOR);


    public AltitudeConfigurationDescription(Context context) {
        super(context);
        setLabel(context);
    }

    private void setLabel(Context context) {
        if (haveSensor) {
            configuration  = " " + context.getString(R.string.sensor_barometer);

        } else if (new SolidAdjustGpsAltitude(context).isEnabled()) {
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
            setLabel(getContext());
        }

        super.onContentUpdated(iid, information);
    }


    @Override
    public String getLabel() {
        return super.getLabel() + configuration;
    }
}
