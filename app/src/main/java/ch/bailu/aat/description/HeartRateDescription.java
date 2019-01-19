package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.bluetooth_le.HeartRateServiceID;
import ch.bailu.aat.util.ToDo;

public class HeartRateDescription extends ContentDescription {
    private static final String LABEL = ToDo.translate("Heart Rate");
    private static final String UNIT = ToDo.translate("bpm");

    private String value = "  ";
    private String unit = UNIT;
    private String label = LABEL;


    public HeartRateDescription(Context c) {
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
        if (iid == InfoID.HEART_RATE_SENSOR) {
            String bpm = info.getAttributes().getValue(HeartRateServiceID.BPM_KEY_INDEX);
            String bpma = info.getAttributes().getValue(HeartRateServiceID.BPMA_KEY_INDEX);
            String contact = info.getAttributes().getValue(HeartRateServiceID.CONTACT_KEY_INDEX);


            value = bpma;
            label = LABEL + " [" + contact + "]";
            unit = UNIT + " [" + bpm + "]";
        }
    }
}
