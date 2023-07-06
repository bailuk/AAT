package ch.bailu.aat.preferences.location

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.services.location.GpsLocation
import ch.bailu.aat.services.location.GpsOrNetworkLocation
import ch.bailu.aat.services.location.MockLocation
import ch.bailu.aat.services.location.NetworkLocation
import ch.bailu.aat.services.location.RealLocation
import ch.bailu.aat.util.AppPermission
import ch.bailu.aat_lib.preferences.location.SolidLocationProvider
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.location.LocationServiceInterface
import ch.bailu.aat_lib.service.location.LocationStackItem

class AndroidSolidLocationProvider(val context: Context) : SolidLocationProvider(
    Storage(context), generateProviderList(context).toTypedArray()
) {

    override fun createProvider(
        locationService: LocationServiceInterface,
        last: LocationStackItem
    ): LocationStackItem {
        val i = index
        return if (i == 1) GpsLocation(last, context, 2000) else if (i == 2) GpsLocation(
            last,
            context,
            3000
        ) else if (i == 3) GpsOrNetworkLocation(
            last,
            context,
            1000
        ) else if (i == 4) NetworkLocation(last, context, 5000) else if (i == 5) MockLocation(
            context, last
        ) else if (i >= PRESETS && i < providerList.size) {
            RealLocation(
                last, context, providerList[i],
                1000
            )
        } else GpsLocation(last, context, 1000)
    }

    override fun getToolTip(): String? {
        return if (!AppPermission.checkLocation(context)) {
            Res.str().p_location_provider_permission()
        } else super.getToolTip()
    }

    companion object {
        private var providerList: ArrayList<String> = ArrayList()
        private const val PRESETS = 6

        private fun generateProviderList(c: Context): List<String> {
            if (providerList.isEmpty()) {
                val providers = RealLocation.getAllLocationProvidersOrNull(c)

                providerList.add(c.getString(R.string.p_location_gps))
                providerList.add(c.getString(R.string.p_location_gps) + " 2000ms")
                providerList.add(c.getString(R.string.p_location_gps) + " 3000ms")
                providerList.add(c.getString(R.string.p_location_gpsnet))
                providerList.add(c.getString(R.string.p_location_network))
                providerList.add(c.getString(R.string.p_location_mock))

                if (providers is List<String>) {
                    providerList.addAll(providers)
                }
            }
            return providerList
        }
    }
}
