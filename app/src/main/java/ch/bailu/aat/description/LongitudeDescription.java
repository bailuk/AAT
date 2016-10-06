package ch.bailu.aat.description;

import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
public class LongitudeDescription extends DoubleDescription {
    public static final String UNIT="\u00B0";

    public LongitudeDescription(Context context) {
        super(context);

    }

    @Override
    public String getLabel() {
        return getString(R.string.d_longitude);
    }

    @Override
    public String getUnit() {
        return UNIT;
    }

    @Override
    public String getValue() {
        return String.format(Locale.getDefault(),"%.6f",  getCache());
    }

    @Override
    public void updateGpxContent(GpxInformation info) {
        setCache(info.getLongitude());
    }
}
