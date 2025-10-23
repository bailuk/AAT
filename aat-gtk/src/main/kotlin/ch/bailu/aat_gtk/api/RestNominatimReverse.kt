package ch.bailu.aat_gtk.api

import ch.bailu.aat_gtk.lib.rest.RestClient
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.util.Limit

/**
 * https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=47.375006&lon=8.530892&zoom=15
 */
class RestNominatimReverse {
    private val restClient = RestClient(
        RestNominatim.getJsonFile("reverse.json"),
        AppConfig.getInstance().userAgent)

    fun search(latLong: LatLongInterface, zoom: Int, observer: (RestClient)->Unit) {
        val zoom = Limit.clamp(zoom, 3, 18)
        val url = "https://nominatim.openstreetmap.org/reverse?format=geocodejson&lat=${latLong.getLatitude()}&lon=${latLong.getLongitude()}&zoom=${zoom}"

        restClient.download(url, observer)
    }
}
