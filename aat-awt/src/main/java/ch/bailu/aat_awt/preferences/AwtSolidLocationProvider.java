package ch.bailu.aat_awt.preferences;

import ch.bailu.aat_awt.services.location.GeoClue2LocationProvider;
import ch.bailu.aat_awt.services.location.ThreadedMockLocation;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.resources.ToDo;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationStackItem;
import ch.bailu.foc.FocFile;

public class AwtSolidLocationProvider extends SolidLocationProvider {

    public AwtSolidLocationProvider(StorageInterface s) {
        super(s, new String[]{"GeoClue2", ToDo.translate("Threaded mock location")});
    }

    @Override
    public LocationStackItem createProvider(LocationServiceInterface locationService, LocationStackItem last) {
        if (getIndex()==0) {
            return new GeoClue2LocationProvider(locationService, last);
        } else {
            return new ThreadedMockLocation(locationService, last, getStorage(), string -> new FocFile(string));
        }

    }
}
