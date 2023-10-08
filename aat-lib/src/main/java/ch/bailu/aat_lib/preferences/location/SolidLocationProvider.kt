package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.preferences.SolidStaticIndexList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.location.LocationStackItem
import javax.annotation.Nonnull

abstract class SolidLocationProvider(storage: StorageInterface, list: Array<String>) :
    SolidStaticIndexList(storage, KEY, list) {

    abstract fun createProvider(
        locationService: LocationServiceInterface,
        last: LocationStackItem
    ): LocationStackItem

    @Nonnull
    override fun getLabel(): String {
        return Res.str().p_location_provider()
    }

    companion object {
        private const val KEY = "location_provider"
    }
}
