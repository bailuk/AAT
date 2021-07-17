package ch.bailu.aat.services.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.location.SolidGpsTimeFix;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.location.LocationInformation;
import ch.bailu.aat_lib.service.location.LocationStackItem;

public final class GpsLocation extends RealLocation {

    private boolean fixTime;


    public GpsLocation(LocationStackItem i, Context c, int interval) {
        super(i, c, LocationManager.GPS_PROVIDER, interval);

        fixTime = new SolidGpsTimeFix(new Storage(c)).getValue();
    }


    @Override
    protected LocationInformation factoryLocationInformation(Location location, int state) {

        LocationInformation l = new GpsLocationInformation(location, state);

        fixGpsTime(location, l.getCreationTime());

        return l;

    }

    @Override
    public void onPreferencesChanged(StorageInterface storage, String key, int presetIndex) {
        fixTime = new SolidGpsTimeFix(storage).getValue();
    }

    private void fixGpsTime(Location l, long systemTime) {
        long time = SolidGpsTimeFix.fix(l.getTime(), systemTime);

        if (fixTime) {

            l.setTime(time);
        }
    }
}
