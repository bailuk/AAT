package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.bluetooth_le.CscServiceID;
import ch.bailu.aat.util.ToDo;

public class CadenceDescription  extends ContentDescription {
    private static final String LABEL = ToDo.translate("Cadence");
    private static final String UNIT = "rpm";

    private String value = "  ";
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
        if (iid == InfoID.CADENCE_SENSOR) {
            value = info.getAttributes().getValue(CscServiceID.KEY_INDEX_CRANK_RPM_AVERAGE);
            unit = UNIT + " " + info.getAttributes().getValue(CscServiceID.KEY_INDEX_CRANK_RPM);
        }
    }
}
