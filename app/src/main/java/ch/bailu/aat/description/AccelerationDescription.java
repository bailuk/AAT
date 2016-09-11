package ch.bailu.aat.description;

import java.util.Locale;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.R;

public class AccelerationDescription extends FloatDescription {

    private static String UNIT="m/s\u00B2";
    
    private Context context;
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
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getAcceleration());
    }
}
