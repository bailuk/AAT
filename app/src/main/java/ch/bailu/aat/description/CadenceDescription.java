package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.sensor.bluetooth_le.CscServiceID;
import ch.bailu.aat.services.sensor.list.SensorState;

public class CadenceDescription  extends ContentDescription {
    private static final String LABEL = SensorState.getName(InfoID.CADENCE_SENSOR);
    private static final String UNIT = "rpm";

    private String value = VALUE_DISABLED;
    private String unit = UNIT;
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
        return unit;
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

        final boolean haveSensor = SensorState.isConnected(InfoID.CADENCE_SENSOR);

        if (iid == InfoID.CADENCE_SENSOR && haveSensor) {
            value = info.getAttributes().getValue(CscServiceID.KEY_INDEX_CRANK_RPM_AVERAGE);
            unit = info.getAttributes().getValue(CscServiceID.KEY_INDEX_CRANK_RPM) + " " + UNIT;
        } else {
            value = VALUE_DISABLED;
            unit = UNIT;
        }
    }
}
