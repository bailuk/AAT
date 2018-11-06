package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
public class LongitudeDescription extends DoubleDescription {
    public static final String UNIT="\u00B0";

    public LongitudeDescription(Context context) {
        super(context);

    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_longitude);
    }

    @Override
    public String getUnit() {
        return UNIT;
    }

    private final FF f = FF.f();

    public String getValue() {
        return f.N6.format(getCache());
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache(info.getLongitude());
    }
}
