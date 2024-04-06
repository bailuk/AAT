package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.foc.Foc
import java.io.IOException
import java.util.Locale

object AppDirectory {
    //////////////////////////////////////////////////////////////////////////////////////
    fun getDataDirectory(sdirectory: SolidDataDirectory, sub: String): Foc {
        return sdirectory.getValueAsFile().descendant(sub)
    }

    const val DIR_AAT_DATA = "aat_data"
    const val DIR_TILES = "tiles"
    const val DIR_TILES_OSMDROID = "osmdroid/tiles"
    private const val DIR_LOG = "log"
    private const val FILE_LOG = "log.gpx"

    @JvmStatic
    fun getLogFile(sdirectory: SolidDataDirectory): Foc {
        return getDataDirectory(sdirectory, DIR_LOG).child(FILE_LOG)
    }

    const val DIR_OVERLAY = "overlay"
    const val DIR_IMPORT = "import"

    const val DIR_NOMINATIM = "query/nominatim"
    const val FILE_NOMINATIM = "nominatim.xml"

    const val DIR_OVERPASS = "query/overpass"
    const val FILE_OVERPASS = "overpass.osm"

    const val DIR_POI = "query/poi"
    const val FILE_POI = "poi.gpx"

    const val DIR_CACHE = "cache"
    const val FILE_CACHE_DB = "summary.db"
    const val FILE_CACHE_MVDB = "summary.mv.db"
    private const val DIR_EDIT = "overlay/draft"
    const val FILE_EDIT_BACKUP = "edit.txt"
    private const val FILE_DRAFT = "draft.gpx"
    const val FILE_SELECTION = "selection.txt"
    fun getEditorDraft(sdirectory: SolidDataDirectory): Foc {
        return getDataDirectory(sdirectory, DIR_EDIT).child(FILE_DRAFT)
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    @JvmStatic
    fun getTileFile(tilePath: String?, sdirectory: SolidTileCacheDirectory): Foc {
        return getTileCacheDirectory(sdirectory).descendant(tilePath)
    }

    private fun getTileCacheDirectory(sdirectory: SolidTileCacheDirectory): Foc {
        return sdirectory.getValueAsFile()
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    const val PRESET_PREFIX = "activity"
    const val PREVIEW_EXTENSION = ".preview"
    fun getTrackListDirectory(sdirectory: SolidDataDirectory, i: Int): Foc {
        return getDataDirectory(sdirectory, getPresetPrefix(i))
    }

    private fun getPresetPrefix(i: Int): String {
        return PRESET_PREFIX + i
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private const val MAX_TRY = 99
    const val GPX_EXTENSION = ".gpx"
    const val OSM_EXTENSION = ".osm"

    @Throws(IOException::class)
    fun generateUniqueFilePath(directory: Foc, prefix: String, extension: String): Foc {
        var file = directory.child(generateFileName(prefix, extension))
        var x = 1
        while (file.exists() && x < MAX_TRY) {
            file = directory.child(generateFileName(prefix, x, extension))
            x++
        }
        if (file.exists()) throw IOException()
        return file
    }

    fun generateDatePrefix(): String {
        val time = System.currentTimeMillis()
        return String.format(
            Locale.ROOT,
            "%tY_%tm_%td_%tH_%tM", time, time, time, time, time
        )
    }

    private fun generateFileName(prefix: String, extension: String): String {
        return prefix + extension
    }

    private fun generateFileName(prefix: String, i: Int, extension: String): String {
        return String.format(
            Locale.ROOT,
            "%s_%d%s", prefix, i, extension
        )
    }

    fun parsePrefix(file: Foc): String {
        val name = StringBuilder(file.name)
        var length = name.length
        for (i in length - 1 downTo 1) {
            if (name[i] == '.') {
                length = i
                break
            }
        }
        name.setLength(length)
        return name.toString()
    }

    fun getGpxDirectories(sdirectory: SolidDataDirectory): Array<Foc> {
        return arrayOf(
            getTrackListDirectory(sdirectory, 0),
            getTrackListDirectory(sdirectory, 1),
            getTrackListDirectory(sdirectory, 2),
            getTrackListDirectory(sdirectory, 3),
            getTrackListDirectory(sdirectory, 4),
            getDataDirectory(sdirectory, DIR_OVERLAY),
            getDataDirectory(sdirectory, DIR_IMPORT)
        )
    }
}
