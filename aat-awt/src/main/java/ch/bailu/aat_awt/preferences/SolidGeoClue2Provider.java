package ch.bailu.aat_awt.preferences;

import ch.bailu.aat_awt.services.location.GeoClue2LocationProvider;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.service.location.LocationService;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationStackItem;

public class SolidGeoClue2Provider extends SolidLocationProvider {

    public SolidGeoClue2Provider(StorageInterface s) {
        super(s, new String[]{"GeoClue2"});
    }

    @Override
    public LocationStackItem createProvider(LocationServiceInterface locationService, LocationStackItem last) {
        return new GeoClue2LocationProvider(locationService, last);
    }
}
