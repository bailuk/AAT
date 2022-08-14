package ch.bailu.aat_gtk.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.lib.rest.RestClient
import ch.bailu.aat_lib.Configuration
import ch.bailu.aat_lib.util.fs.AppDirectory
import java.io.File

class RestNominatim {
    private val search = RestClient(getJsonFile("search"),Configuration.appName,"{\"result\":","}")

    fun search(search: String, observer: (RestClient)->Unit) {
        val url = "https://nominatim.openstreetmap.org/search"
        this.search.download("${url}?city=${search}&format=json", observer)
    }

    private fun getJsonFile(name: String): File {
        val dir = AppDirectory.getDataDirectory(
            GtkAppContext.dataDirectory,
            AppDirectory.DIR_NOMINATIM)
        dir.mkdirs()
        return File(dir.child(name).toString())
    }
}
