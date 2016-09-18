package ch.bailu.aat.description;

import java.util.Locale;

import android.content.Context;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.R;

public class BearingDescription extends DoubleDescription {
    private final Context context;
    public BearingDescription(Context c) {
        super(c);
        context=c;
    }

    @Override
    public String getLabel() {
        return context.getString(R.string.d_bearing);
    }

    @Override
    public String getValue() {
        return String.format((Locale)null, "%.3f",  getCache());
    }
    
    @Override
    public String getUnit() {
        return LongitudeDescription.UNIT;
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getBearing());
    }

   
    

}
