package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.gpx.GpxInformation;

public class AccelerationDescription extends FloatDescription {

    private final static String UNIT="m/s\u00B2";

    private final Context context;
    public AccelerationDescription(Context c) {
        context=c;
    }

    @Override
    public String getLabel() {
        return context.getString(R.string.d_acceleration);
    }

    public String getUnit() {
        return UNIT;
    }


    public String getValue() {
        return FF.f().N2.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getAcceleration());
    }
}