package ch.bailu.aat.description;

import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

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
    public void onContentUpdated(GpxInformation info) {
        setCache(info.getBearing());
    }

   
    

}
