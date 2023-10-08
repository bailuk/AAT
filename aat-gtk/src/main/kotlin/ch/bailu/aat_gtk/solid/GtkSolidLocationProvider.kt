package ch.bailu.aat_gtk.solid

import ch.bailu.aat_gtk.service.location.GeoClue2LocationProvider
import ch.bailu.aat_gtk.service.location.ThreadedMockLocation
import ch.bailu.aat_gtk.service.location.directory.GPSdLocationProvider
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.location.LocationStackItem
import ch.bailu.foc.FocFile

class GtkSolidLocationProvider (storage: StorageInterface) :
    SolidLocationProvider(storage, arrayOf("GeoClue2", "GPSd", Res.str().p_location_mock()))
{

    override fun createProvider(
        locationService: LocationServiceInterface,
        last: LocationStackItem
    ): LocationStackItem {

        return if (index == 0) {
            GeoClue2LocationProvider(last)
        } else if (index == 1) {
            GPSdLocationProvider(last)
        } else {
            ThreadedMockLocation(locationService, last,getStorage()) { string: String? -> FocFile(string) }
        }
    }
}
