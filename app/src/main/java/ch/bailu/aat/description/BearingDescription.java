package ch.bailu.aat.description;

import android.content.Context;

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

    private final FF f = FF.f();

    public String getValue() {
        return f.N3.format(getCache());
    }
    
    @Override
    public String getUnit() {
        return LongitudeDescription.UNIT;
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
//        setCache(info.getBearing());
    }

   
    

}
