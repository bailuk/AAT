package ch.bailu.aat_gtk.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.lib.rest.RestClient
import ch.bailu.aat_lib.app.AppConfig
import ch.bailu.aat_lib.util.fs.AppDirectory
import java.io.File

class RestNominatim {
    val restClient = RestClient(getJsonFile("search"), AppConfig.getInstance().userAgent,"{\"result\":","}")

    fun search(search: String, observer: (RestClient)->Unit) {
        val url = "https://nominatim.openstreetmap.org/search"
        this.restClient.download("${url}?city=${search}&format=json", observer)
    }

    private fun getJsonFile(name: String): File {
        val dir = AppDirectory.getDataDirectory(
            GtkAppContext.dataDirectory,
            AppDirectory.DIR_NOMINATIM)
        dir.mkdirs()
        return File(dir.child(name).toString())
    }

}
