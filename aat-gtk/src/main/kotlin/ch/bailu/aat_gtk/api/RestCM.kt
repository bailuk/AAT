package ch.bailu.aat_gtk.api

import ch.bailu.aat_gtk.lib.rest.RestClient
import ch.bailu.aat_lib.app.AppConfig

class RestCM {
    private val url = "https://api-gw.criticalmaps.net/locations"
    private val restClient = RestClient(
        RestNominatim.getJsonFile("cm.json"),
        AppConfig.getInstance().userAgent)

    fun search(observer: (RestClient)->Unit) {
        restClient.download(url, observer)
    }
}
