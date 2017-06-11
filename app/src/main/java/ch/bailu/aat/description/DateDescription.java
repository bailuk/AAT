package ch.bailu.aat.description;


import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class DateDescription extends LongDescription {
    public DateDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_startdate);
    }
    public String getTime()   {
        return format(getCache());
    }

    public static String format(long time) {
        return String.format((Locale) null, "%tF - %tT",time, time);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getStartTime());
    }

}
