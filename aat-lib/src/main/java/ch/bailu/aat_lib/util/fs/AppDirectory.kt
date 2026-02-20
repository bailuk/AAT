package ch.bailu.aat_lib.util.fs

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.map.SolidTileCacheDirectory
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc

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

    const val DIR_QUERY = "query"
    const val DIR_NOMINATIM = "${DIR_QUERY}/nominatim"
    const val FILE_NOMINATIM = "nominatim.xml"
    const val FILE_NOMINATIM_REVERSE = "reverse.json"

    const val DIR_OVERPASS = "${DIR_QUERY}/overpass"
    const val FILE_OVERPASS = "overpass.xml" // Extension .osm is not compatible with Android SAF

    const val DIR_POI = "${DIR_QUERY}/poi"
    const val FILE_POI = "poi.gpx"

    const val FILE_CRITICAL_MAP = "cm.json"
    const val FILE_BROUTER = "brouter.gpx"

    const val DIR_CACHE = "cache"
    const val FILE_CACHE_DB = "summary.db"
    const val FILE_CACHE_MVDB = "summary.mv.db"
    const val DIR_EDIT = "overlay/draft"
    const val FILE_EDIT_BACKUP = "edit.txt"
    const val FILE_DRAFT = "draft.gpx"
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
    const val GPX_EXTENSION = ".gpx"
    const val OSM_EXTENSION = ".xml" // Extension .osm is not compatible with Android SAF


    /**
     * List of gpx directories as name / directory pair
     * All presets (according to SolidPreset) and overlay and import directories
     */
    fun getGpxDirectories(appContext: AppContext): ArrayList<GpxDirectoryEntry> {
        val result = ArrayList<GpxDirectoryEntry>()
        val solidPreset = SolidPreset(appContext.storage)

        for (index in 0 until  solidPreset.length()) {
            val name = solidPreset.getValueAsString(index)
            val directory = getTrackListDirectory(appContext.dataDirectory, index)
            result.add(GpxDirectoryEntry(name, directory))
        }
        getDataDirectory(appContext.dataDirectory, DIR_OVERLAY).apply {
            result.add(GpxDirectoryEntry(Res.str().p_overlay(), this))
        }
        getDataDirectory(appContext.dataDirectory, DIR_IMPORT).apply {
            result.add(GpxDirectoryEntry(this.name, this))
        }
        return result
    }

    fun getMimeTypeFromFileName(name: String): String {
        return if (name.endsWith(GPX_EXTENSION)) {
            "application/gpx+xml" }
        else if (name.endsWith(OSM_EXTENSION)) {
            "application/xml"
        } else {
            return "application/octet-stream"
        }
    }

    data class GpxDirectoryEntry(val name: String, val file: Foc)
}
