package ch.bailu.aat.util

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.Locale

abstract class OverpassApi(context: Context, b: BoundingBoxE6) : DownloadApi() {
    final override val apiName: String
    private val bounding: String
    final override val baseDirectory: Foc
    override val urlStart: String
        get() = "$URL…"

    override val fileExtension = ".osm"

    init {
        apiName = getName(context)
        bounding = toString(b)
        baseDirectory = AppDirectory.getDataDirectory(
            AndroidSolidDataDirectory(context),
            AppDirectory.DIR_OVERPASS
        )
    }

    /**
     * See: http://overpass-api.de/command_line.html
     * Create an encoded URL for Overpass query
     * @throws UnsupportedEncodingException if UTF-8 is not supported by the URLEncoder
     */
    @Throws(UnsupportedEncodingException::class)
    override fun getUrl(query: String): String {
        val queries = query.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val url = StringBuilder()
        url.setLength(0)
        url.append(URL)
        for (q in queries) {
            val qTrimmed = q.trim { it <= ' ' }
            if (qTrimmed.isNotEmpty()) {
                if (qTrimmed[0] == '[') {
                    url.append("node")
                    url.append(URLEncoder.encode(qTrimmed, UTF8))
                    url.append(URLEncoder.encode(bounding, UTF8))
                    url.append("rel")
                    url.append(URLEncoder.encode(qTrimmed, UTF8))
                    url.append(URLEncoder.encode(bounding, UTF8))
                    url.append("way")
                    url.append(URLEncoder.encode(qTrimmed, UTF8))
                    url.append(URLEncoder.encode(bounding, UTF8))
                } else {
                    url.append(URLEncoder.encode(qTrimmed, UTF8))
                    url.append(URLEncoder.encode(bounding, UTF8))
                }
            }
        }
        url.append(URLEncoder.encode(POST, UTF8))
        return url.toString()
    }

    override fun getUrlPreview(query: String): String {
        val queries = query.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val url = StringBuilder()
        url.setLength(0)
        url.append(URL)
        url.append('\n')
        for (q in queries) {
            val qTrimmed = q.trim { it <= ' ' }
            if (qTrimmed.isNotEmpty()) {
                if (qTrimmed[0] == '[') {
                    url.append("node")
                    url.append(qTrimmed)
                    url.append(bounding)
                    url.append('\n')
                    url.append("rel")
                    url.append(qTrimmed)
                    url.append(bounding)
                    url.append('\n')
                    url.append("way")
                    url.append(qTrimmed)
                    url.append(bounding)
                    url.append('\n')
                } else {
                    url.append(qTrimmed)
                    url.append(bounding)
                    url.append('\n')
                }
            }
        }
        url.append(POST)
        return url.toString()
    }



    companion object {
        private const val UTF8 = "UTF-8"


        const val URL = "https://overpass-api.de/api/interpreter?data=(" // data=node
        const val POST = ">;);out;"

        fun getName(context: Context): String {
            return context.getString(R.string.query_overpass)
        }

        private fun toString(bounding: BoundingBoxE6): String {
            val lo1 = bounding.lonWestE6 / 1E6
            val la1 = bounding.latSouthE6 / 1E6
            val lo2 = bounding.lonEastE6 / 1E6
            val la2 = bounding.latNorthE6 / 1E6
            return String.format(
                Locale.ROOT, "(%.2f,%.2f,%.2f,%.2f);",
                Math.min(la1, la2),
                Math.min(lo1, lo2),
                Math.max(la1, la2),
                Math.max(lo1, lo2)
            )
        }
    }
}
