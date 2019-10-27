package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.attributes.CadenceSpeedAttributes;
import ch.bailu.aat.services.sensor.list.SensorState;

public class CadenceDescription  extends ContentDescription {
    public static final String UNIT = "rpm";
    public static final String LABEL = SensorState.getName(InfoID.CADENCE_SENSOR);
    public static final String LABEL_WAIT = LABEL + "...";


    private String value = VALUE_DISABLED;
    private String label = LABEL;


    public CadenceDescription(Context c) {
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
        return UNIT;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {


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
