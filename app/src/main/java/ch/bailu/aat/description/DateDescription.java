package ch.bailu.aat.description;


import android.content.Context;

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
    public String getValue()   {
        return format(getCache());
    }

    public static String format(long time) {
        return String.format("%tc",time);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getStartTime());
    }

}
