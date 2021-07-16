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

    @Override
    public String getValue()   {
        return FF.f().LOCAL_DATE_TIME.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getStartTime());
    }

}
