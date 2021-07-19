package ch.bailu.aat_lib.description;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.PowerAttributes;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.sensor.SensorState;

public class PowerDescription  extends ContentDescription {
    public static final String UNIT = "W";
    private final String LABEL, LABEL_WAIT;

    private String value = VALUE_DISABLED;
    private String label;

    public PowerDescription() {
        LABEL = Res.str().sensor_power();
        LABEL_WAIT = LABEL + "...";
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
        final boolean haveSensor = SensorState.isConnected(InfoID.POWER_SENSOR);

        if (iid == InfoID.POWER_SENSOR && haveSensor) {
            final boolean hasContact = info.getAttributes().getAsBoolean(PowerAttributes.KEY_INDEX_CONTACT);

            if (hasContact) {
                label = LABEL;
            } else {
                label = LABEL_WAIT;
            }

            value = info.getAttributes().get(PowerAttributes.KEY_INDEX_POWER);
        } else {
            label = LABEL;
            value = VALUE_DISABLED;
        }
    }
}
