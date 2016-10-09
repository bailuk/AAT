package ch.bailu.aat.description;

import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class AccelerationDescription extends FloatDescription {

    private final static String UNIT="m/s\u00B2";
    
    private final Context context;
    public AccelerationDescription(Context c) {
        super(c);
        context=c;
    }

    @Override
    public String getLabel() {
        return context.getString(R.string.d_acceleration);
    }

    public String getUnit() {
        return UNIT; 
    }
    
    @Override
    public String getValue() {
        return String.format(Locale.getDefault(),"%.2f", getCache());
    }

    @Override
    public void onContentUpdated(GpxInformation info) {
        setCache(info.getAcceleration());
    }
}
