package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.PowerAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

public class PowerDescription  extends ContentDescription {
    public static final String UNIT = "W";
    private final String LABEL, LABEL_WAIT;

    private String value = VALUE_DISABLED;
    private String label;

    public PowerDescription(Context c) {
        super(c);
        LABEL = c.getString(R.string.sensor_power);
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
