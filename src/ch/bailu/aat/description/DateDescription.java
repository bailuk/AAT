package ch.bailu.aat.description;


import java.util.Locale;

import android.content.Context;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.R;

public class DateDescription extends LongDescription {
    public DateDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getString(R.string.d_startdate);
    }
    @Override
    public String getValue()   { 
        return String.format((Locale) null, "%tF - %tT",getCache(), getCache());
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getStartTime());
    }

}
