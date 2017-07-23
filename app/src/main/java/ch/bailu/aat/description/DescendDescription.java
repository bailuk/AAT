package ch.bailu.aat.description;

import android.content.Context;

import java.util.Locale;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class DescendDescription extends AltitudeDescription {
    public DescendDescription(Context context) {
        super(context);
    }

    @Override
    public String getLabel() {
        return "Descend*";
    }



    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        setCache( ((float)info.getDescend()) );
    }
}
