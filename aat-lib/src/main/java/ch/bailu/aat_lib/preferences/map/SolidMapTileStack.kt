package ch.bailu.aat_lib.preferences.map

import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource
import ch.bailu.aat_lib.preferences.SolidBoolean
import ch.bailu.aat_lib.preferences.SolidCheckList
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.service.cache.DownloadSource


class SolidMapTileStack private constructor(srender: SolidRenderTheme, preset: Int) :
    SolidCheckList() {
    private val enabledArray = arrayOfNulls<SolidBoolean>(SOURCES.size)
    private val srenderTheme: SolidRenderTheme

    constructor(srender: SolidRenderTheme) : this(srender, 0)

    // FIXME: use preset for tile stack
    init {
        for (i in enabledArray.indices) {
            enabledArray[i] = SolidBoolean(srender.getStorage(), KEY + preset + "_" + i)
        }
        srenderTheme = srender
    }

    override fun getStringArray(): Array<String> {
        val mapsForgeLabel = (MapsForgeSource.NAME
                + " " + srenderTheme.valueAsThemeName)

        val result = ArrayList<String>(SOURCES.size)
        result.add(mapsForgeLabel)

        for (i in 1 until SOURCES.size) result.add(SOURCES[i].name)
        return result.toTypedArray()
    }

    override fun getEnabledArray(): BooleanArray {
        val array = BooleanArray(enabledArray.size)
        for (i in enabledArray.indices) array[i] = enabledArray[i]!!.isEnabled
        return array
    }

    override fun setEnabled(index: Int, isChecked: Boolean) {
        var i = index
        i = Math.min(enabledArray.size, i)
        i = Math.max(0, i)
        enabledArray[i]!!.value = isChecked
    }


    override fun getLabel(): String {
        return Res.str().p_map()
    }

    override fun getKey(): String {
        return KEY
    }

    override fun getStorage(): StorageInterface {
        return enabledArray[0]!!.getStorage()
    }

    override fun hasKey(key: String): Boolean {
        for (anEnabledArray in enabledArray) {
            if (anEnabledArray!!.hasKey(key)) {
                return true
            }
        }
        return false
    }

    fun setDefaults() {
        for (i in SOURCES.indices) {
            if (SOURCES[i] === DownloadSource.MAPNIK) {
                setEnabled(i, true)
                break
            }
        }
    }

    companion object {
        private const val KEY = "tile_overlay_"
        const val FIRST_OVERLAY_INDEX = 4
        @JvmField
        val SOURCES = arrayOf(
            MapsForgeSource.MAPSFORGE,
            DownloadSource.MAPNIK,
            DownloadSource.OPEN_TOPO_MAP,
            DownloadSource.CYCLE_OSM_MAP,
            DownloadSource.OPEN_CYCLE_MAP,
            DownloadSource.TRANSPORT_OVERLAY,
            ElevationSource.ELEVATION_COLOR,
            ElevationSource.ELEVATION_HILLSHADE,
            DownloadSource.TRAIL_SKATING,
            DownloadSource.TRAIL_HIKING,
            DownloadSource.TRAIL_MTB,
            DownloadSource.TRAIL_CYCLING
        )
    }
}
