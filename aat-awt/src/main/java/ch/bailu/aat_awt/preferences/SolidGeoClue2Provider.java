package ch.bailu.aat_awt.preferences;

import org.freedesktop.dbus.exceptions.DBusException;

import ch.bailu.aat_awt.services.location.GeoClue2LocationProvider;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider;
import ch.bailu.aat_lib.service.location.LocationStackChainedItem;
import ch.bailu.aat_lib.service.location.LocationStackItem;

public class SolidGeoClue2Provider extends SolidLocationProvider {

    public SolidGeoClue2Provider(StorageInterface s) {
        super(s, new String[]{"GeoClue2"});
    }

    @Override
    public LocationStackItem createProvider(LocationStackItem last) {
        try {
            return new GeoClue2LocationProvider(last);
        } catch (DBusException e) {
            return new LocationStackChainedItem(last);
        }
    }
}
