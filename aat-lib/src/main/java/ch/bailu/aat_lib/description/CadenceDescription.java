package ch.bailu.aat_lib.description;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.sensor.SensorState;

public class CadenceDescription  extends ContentDescription {
    public static final String UNIT = "rpm";
    private final String LABEL, LABEL_WAIT;

    private String value = VALUE_DISABLED;
    private String label;

    public CadenceDescription() {
        LABEL = Res.str().sensor_cadence();
        LABEL_WAIT = LABEL + "…";
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
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        final boolean haveSensor = SensorState.isConnected(InfoID.CADENCE_SENSOR);

        if (iid == InfoID.CADENCE_SENSOR && haveSensor) {
            final boolean hasContact = info.getAttributes().getAsBoolean(CadenceSpeedAttributes.KEY_INDEX_CONTACT);

            if (hasContact) {
                label = LABEL;
            } else {
                label = LABEL_WAIT;
            }
            value = info.getAttributes().get(CadenceSpeedAttributes.KEY_INDEX_CRANK_RPM);
        } else {
            label = LABEL;
            value = VALUE_DISABLED;
        }
    }
}
