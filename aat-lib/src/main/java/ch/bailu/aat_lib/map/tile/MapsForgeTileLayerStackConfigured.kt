package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.source.CachedSource
import ch.bailu.aat_lib.map.tile.source.DoubleSource
import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.service.cache.DownloadSource
import org.mapsforge.map.view.MapView

abstract class MapsForgeTileLayerStackConfigured(
    private val mapView: MapView,
    protected val scontext: AppContext
) : MapsForgeTileLayerStack(
    scontext.services
) {
    private val stheme: SolidRenderTheme = SolidRenderTheme(scontext.mapDirectory, scontext)
    private val stiles = SolidMapTileStack(stheme)
    private val scacheHS = SolidEnableTileCache.MapsForge(scontext.storage)
    private val scacheMF = SolidEnableTileCache.HillShade(scontext.storage)

    init {
        initLayers()
    }

    private fun initLayers() {
        val enabled = stiles.getEnabledArray()
        val sources = SolidMapTileStack.SOURCES
        removeLayers()
        addBackgroundLayers(enabled, sources)
        addOverlayLayers(enabled, sources)
        setMapViewZoomLimit(mapView)
    }

    protected abstract fun addBackgroundLayers(enabled: BooleanArray, sources: Array<Source>)
    protected abstract fun addOverlayLayers(enabled: BooleanArray, sources: Array<Source>)
    protected val hillShadeSource: Source
        get() = if (scacheHS.isEnabled) {
            CachedSource.CACHED_ELEVATION_HILLSHADE
        } else ElevationSource.ELEVATION_HILLSHADE
    protected val mapsForgeSource: Source
        get() {
            val theme = stheme.getValueAsString()
            return if (scacheMF.isEnabled) {
                CachedSource(MapsForgeSource(theme))
            } else MapsForgeSource(theme)
        }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (stiles.hasKey(key) ||
            scacheMF.hasKey(key) ||
            scacheHS.hasKey(key) ||
            stheme.hasKey(key)
        ) {
            initLayers()
        }
    }

    open class BackgroundOnly(v: MapView, appContext: AppContext) :
        MapsForgeTileLayerStackConfigured(v, appContext) {
        override fun addBackgroundLayers(enabled: BooleanArray, sources: Array<Source>) {
            var download: Source? = null
            var mapsforge: Source? = null
            for (i in enabled.indices) {
                if (enabled[i]) {
                    if (DownloadSource.isDownloadBackgroundSource(sources[i])) download = sources[i]
                }
            }
            if (enabled[0] && sources[0] === MapsForgeSource.MAPSFORGE) {
                mapsforge = mapsForgeSource
            }
            if (download != null && mapsforge != null) {
                addLayer(
                    TileProvider(
                        scontext,
                        DoubleSource(scontext.services, mapsforge, download, 6)
                    ), scontext.tilePainter
                )
            } else if (download != null) {
                addLayer(TileProvider(scontext, download), scontext.tilePainter)
            } else if (mapsforge != null) {
                addLayer(TileProvider(scontext, mapsforge), scontext.tilePainter)
            }
        }

        override fun addOverlayLayers(enabled: BooleanArray, sources: Array<Source>) {}
    }

    class All(v: MapView, appContext: AppContext) : BackgroundOnly(v, appContext) {
        override fun addOverlayLayers(enabled: BooleanArray, sources: Array<Source>) {
            for (i in SolidMapTileStack.FIRST_OVERLAY_INDEX until sources.size) {
                if (enabled[i]) {
                    var s = sources[i]
                    if (s === ElevationSource.ELEVATION_HILLSHADE) {
                        s = hillShadeSource
                    }
                    addLayer(TileProvider(scontext, s), scontext.tilePainter)
                }
            }
        }
    }
}
