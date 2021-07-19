package ch.bailu.aat.description;

import ch.bailu.aat.coordinates.CH1903Coordinates;
import ch.bailu.aat_lib.description.DoubleDescription;
import ch.bailu.aat_lib.description.FF;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.resources.Res;

public class CH1903EastingDescription extends DoubleDescription {

    protected int coordinate=0;

    @Override
    public String getLabel() {
        return Res.str().d_chy();
    }

    @Override
    public String getUnit() {
        return "km";
    }

    public String getValue() {
        return FF.f().N3.format( ((float)(coordinate) ) / 1000f );
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (setCache(info.getLongitude())) setCH1903_y(info);
    }

    private void setCH1903_y(GpxInformation info) {
        coordinate = new CH1903Coordinates(info.getLatitude(), info.getLongitude()).getEasting();
    }
}
