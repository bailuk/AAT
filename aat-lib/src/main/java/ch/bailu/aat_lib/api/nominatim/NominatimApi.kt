package ch.bailu.aat_lib.api.nominatim

import ch.bailu.aat_lib.api.DownloadApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.preferences.map.SolidNominatimOverlay
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException
import java.lang.Double
import java.net.URLEncoder

abstract class NominatimApi(context: AppContext) : DownloadApi() {
    private val overlay = SolidNominatimOverlay(context.dataDirectory)

    override val urlStart = "https://nominatim.openstreetmap.org/search?q="
    override val fileExtension = ".xml"

    final override val apiName: String
        get() = overlay.getLabel()

    override val resultFile: Foc
        get() = overlay.getValueAsFile()

    override val baseDirectory: Foc
        get() = overlay.directory

    override fun getUrlPreview(query: String, bounding: BoundingBoxE6): String {
        val url = StringBuilder()
        url.setLength(0)
        url.append(urlStart)
        url.append(query)
        url.append(POST)
        url.append(toString(bounding))
        return url.toString()
    }

    @Throws(UnsupportedEncodingException::class)
    override fun getUrl(query: String, bounding: BoundingBoxE6): String {
        val url = StringBuilder()
        url.setLength(0)
        url.append(urlStart)
        url.append(URLEncoder.encode(query.replace('\n', ' '), "UTF-8"))
        url.append(POST)
        url.append(toString(bounding))
        return url.toString()
    }

    companion object {
         const val POST = "&format=xml"
        private fun toString(b: BoundingBoxE6): String {
            return if (b.latitudeSpanE6 > 0 && b.longitudeSpanE6 > 0) {
                "&bounded=1&viewbox=" +
                        toS(b.lonWestE6) + "," +
                        toS(b.latNorthE6) + "," +
                        toS(b.lonEastE6) + "," +
                        toS(b.latSouthE6)
            } else {
                ""
            }
        }

        private fun toS(i: Int): String {
            val d = i.toDouble()
            return Double.valueOf(d / 1E6).toString()
        }
    }
}
