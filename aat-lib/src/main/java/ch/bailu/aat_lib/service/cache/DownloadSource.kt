package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.util.fs.AppDirectory.getTileFile
import org.mapsforge.core.model.Tile
import java.util.Random

open class DownloadSource(private val name: String, private val apiKey: String,  private val minZoom: Int, private val maxZoom: Int, a: Int, vararg u: String) : Source() {
    private val urls: Array<out String> = u
    private val alpha: Int = a
    private val transparent: Boolean = (a != OPAQUE)

    constructor(n: String, a: Int, vararg url: String) : this(n, "", MIN_ZOOM, MAX_ZOOM, a, *url)
    constructor(n: String, k: String, a: Int, vararg url: String) : this(n, k, MIN_ZOOM, MAX_ZOOM, a, *url)
    constructor(n: String, minZ: Int, maxZ: Int, a: Int, vararg url: String) : this(n, "", minZ, maxZ, a, *url)

    override fun getName(): String {
        return name
    }

    override fun getID(tile: Tile, context: AppContext): String {
        return getTileFile(genRelativeFilePath(tile, name), context.tileCacheDirectory).path
    }

    override fun getMinimumZoomLevel(): Int {
        return minZoom
    }

    override fun getMaximumZoomLevel(): Int {
        return maxZoom
    }

    override fun isTransparent(): Boolean {
        return transparent
    }

    override fun getAlpha(): Int {
        return alpha
    }

    override fun getFactory(t: Tile): Obj.Factory {
        return ObjTileDownloadable.Factory(t, this)
    }

    fun getTileURLString(tile: Tile): String {
        return baseUrl + tile.zoomLevel + "/" + tile.tileX + "/" + tile.tileY + EXT + apiKey
    }

    private val baseUrl: String
        get() = urls[random.nextInt(urls.size)]

    companion object {
        private val random = Random()

        const val MIN_ZOOM: Int = 1
        const val MAX_ZOOM: Int = 17 // 18 takes way too much space for the gain.


        fun isDownloadBackgroundSource(source: Source): Boolean {
            return (source === MAPNIK || source === OPEN_TOPO_MAP || source === OPEN_CYCLE_MAP || source === CYCLE_OSM_MAP)
        }

        val MAPNIK: DownloadSource = object : DownloadSource(
            "Mapnik",
            OPAQUE,
            "https://a.tile.openstreetmap.org/",
            "https://b.tile.openstreetmap.org/",
            "https://c.tile.openstreetmap.org/"
        ) {
            override fun filterBitmap(): Boolean {
                return true
            }
        }

        val CYCLE_OSM_MAP: DownloadSource = object : DownloadSource(
            "CyclOSM",
            OPAQUE,
            "https://a.tile-cyclosm.openstreetmap.fr/cyclosm/",
            "https://b.tile-cyclosm.openstreetmap.fr/cyclosm/",
            "https://c.tile-cyclosm.openstreetmap.fr/cyclosm/",
        ) {
            override fun filterBitmap(): Boolean {
                return true
            }
        }

        val OPEN_TOPO_MAP: DownloadSource = object : DownloadSource(
            "OpenTopoMap",
            OPAQUE,
            "https://a.tile.opentopomap.org/",
            "https://b.tile.opentopomap.org/",
            "https://c.tile.opentopomap.org/"
        ) {
            override fun filterBitmap(): Boolean {
                return true
            }
        }

        val OPEN_CYCLE_MAP: DownloadSource = object : DownloadSource(
            "OpenCycleMap",
            "?apikey=4fc8425f35f44f11a59407ef5de1e2c2",
            OPAQUE,
            "https://tile.thunderforest.com/cycle/"
        ) {
            override fun filterBitmap(): Boolean {
                return true
            }
        }

        val TRAIL_MTB: DownloadSource = DownloadSource(
            "TrailMTB",
            TRANSPARENT,
            "https://tile.waymarkedtrails.org/mtb/"
        )

        val TRAIL_SKATING: DownloadSource = DownloadSource(
            "TrailSkating",
            TRANSPARENT,
            "https://tile.waymarkedtrails.org/skating/"
        )

        val TRAIL_HIKING: DownloadSource = DownloadSource(
            "TrailHiking",
            TRANSPARENT,
            "https://tile.waymarkedtrails.org/hiking/"
        )

        val TRAIL_CYCLING: DownloadSource = DownloadSource(
            "TrailCycling",
            TRANSPARENT,
            "https://tile.waymarkedtrails.org/cycling/"
        )

        // https://wiki.openstreetmap.org/wiki/Openptmap Seems to be gone
        // Use https://www.öpnvkarte.de/ instead
        val TRANSPORT_OVERLAY: DownloadSource = DownloadSource(
            "OePNVKarte",
            5, 16,
            OPAQUE,
            "https://tileserver.memomaps.de/tilegen/"
        )
    }
}
