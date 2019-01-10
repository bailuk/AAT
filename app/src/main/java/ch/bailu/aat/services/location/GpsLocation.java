package ch.bailu.aat.services.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import ch.bailu.aat.preferences.location.SolidGpsTimeFix;

public class GpsLocation extends RealLocation {
    private boolean fixTime;


    public GpsLocation(LocationStackItem i, Context c, int interval) {
        super(i, c, LocationManager.GPS_PROVIDER, interval);

        fixTime = new SolidGpsTimeFix(c).getValue();
    }


    @Override
    protected LocationInformation factoryLocationInformation(Location location, int state) {

        LocationInformation l = new GpsLocationInformation(location, state);

        fixGpsTime(location, l.getCreationTime());

        return l;

    }

    @Override
    public void preferencesChanged(Context c, String key, int presetIndex) {
        fixTime = new SolidGpsTimeFix(c).getValue();
    }

    private void fixGpsTime(Location l, long systemTime) {
        long time = SolidGpsTimeFix.fix(l.getTime(), systemTime);

        if (fixTime) {
            l.setTime(time);
        }
    }
}
