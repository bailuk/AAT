package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat.gpx.GpxInformation;

public class CH1903EastingDescription extends DoubleDescription {

    protected int coordinate=0;
    
    public CH1903EastingDescription(Context c) {
        super(c);
    }

    @Override
    public String getLabel() {
        return getContext().getString(R.string.d_chy);
    }

    @Override
    public String getUnit() {
        return "km";
    }

    public String getValue() {
        return FF.N_3.format( ((float)(coordinate) ) / 1000f );
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (setCache(info.getLongitude())) setCH1903_y(info);
    }

    private void setCH1903_y(GpxInformation info) {
        coordinate = new CH1903Coordinates(info.getLatitude(), info.getLongitude()).getEasting();
    }
}
