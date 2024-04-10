package ch.bailu.aat_gtk.view.map.preview

import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.lib.color.ColorInterface
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayer
import ch.bailu.aat_lib.map.tile.TileProvider
import ch.bailu.aat_lib.map.tile.source.CacheOnlySource
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.service.cache.DownloadSource
import ch.bailu.aat_lib.service.directory.MapPreviewInterface
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Dimension
import org.mapsforge.core.model.MapPosition
import org.mapsforge.core.model.Point
import org.mapsforge.map.util.LayerUtil
import org.mapsforge.map.util.MapPositionUtil
import org.mapsforge.map.view.FrameBuffer
import org.mapsforge.map.view.FrameBufferHA3

class MapsForgePreview(private val appContext: AppContext,
                       info: GpxInformation,
                       out: Foc
) : GtkCustomMapView(appContext, Dispatcher(), MapsForgePreview::class.java.simpleName), MapPreviewInterface {
    private val imageFile: Foc
    private val provider: TileProvider
    private val mapPosition: MapPosition
    private val bounding: BoundingBox
    private val tlPoint: Point

    init {

        // Interface: Resize models
        onResize(BITMAP_SIZE, BITMAP_SIZE)

        model.mapViewDimension.dimension = DIM
        imageFile = out
        provider = TileProvider(appContext, getSource(SolidRenderTheme(appContext.mapDirectory, appContext)))

        val tileLayer = MapsForgeTileLayer(appContext.services, provider, appContext.tilePainter)
        add(tileLayer)

        val gpxLayer = GpxDynLayer(appContext.storage, getMContext(), appContext.services)
        add(gpxLayer)
        // Interface
        // attachLayers()
        gpxLayer.onContentUpdated(InfoID.FILE_VIEW, info)
        frameBounding(info.gpxList.getDelta().getBoundingBox())
        mapPosition = model.mapViewPosition.mapPosition

        val tileSize = model.displayModel.tileSize
        bounding = MapPositionUtil.getBoundingBox(mapPosition, DIM, tileSize)
        tlPoint = MapPositionUtil.getTopLeftPoint(mapPosition, DIM, tileSize)
        preLoadTiles()
    }

    private fun preLoadTiles() {
        val tilePositions = LayerUtil.getTilePositions(
            bounding,
            mapPosition.zoomLevel, tlPoint,
            model.displayModel.tileSize
        )
        provider.onAttached()
        provider.preload(tilePositions)
    }

    /**
     *
     * Begin of "prevent MapView from drawing" hack
     * FIXME:
     * This hack prevents the map view from calling the layers draw() function.
     * The correct implementation would be to port the preview generator away from the MapView and
     * just use the MapViews model.
     */
    override fun getFrameBuffer(): FrameBuffer {
        return object : FrameBufferHA3(
            model.frameBufferModel,
            model.displayModel,
            AppGraphicFactory.instance()
        ) {
            override fun getDrawingBitmap(): Bitmap? {
                return null
            }
        }
    }

    override fun repaint() {}
    override fun requestRedraw() {}

    /**
     * End of "prevent MapView from drawing" hack
     */

    private fun generateBitmap(): MapTileInterface {
        val bitmap = appContext.createMapTile()

        bitmap.set(BITMAP_SIZE, false)
        if (bitmap.isLoaded()) {
            val canvas = bitmap.getCanvas()
            bitmap.getBitmap()?.setBackgroundColor(ColorInterface.BLACK)
            for (layer in layerManager.layers) {
                layer.draw(bounding, mapPosition.zoomLevel, canvas, tlPoint)
            }
        }
        return bitmap
    }

    override fun generateBitmapFile() {
        val bitmap = generateBitmap()
        try {
            imageFile.openW()?.use { out ->
                bitmap.getBitmap()?.compress(out)
                appContext.broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    imageFile.path,
                    javaClass.name
                )
            }
        } catch (e: Exception) {
            AppLog.e(this, e)
        }
        bitmap.free()
    }

    override fun isReady(): Boolean {
        return provider.isReadyAndLoaded
    }

    override fun onDestroy() {
        provider.onDetached()
        super.onDestroy()
    }

    companion object {
        const val BITMAP_SIZE = 128
        private val DIM = Dimension(BITMAP_SIZE, BITMAP_SIZE)
        private val MAPNIK: Source = CacheOnlySource(DownloadSource.MAPNIK)

        private fun getSource(stheme: SolidRenderTheme): Source {
            val tiles = SolidMapTileStack(stheme)
            val enabled = tiles.getEnabledArray()
            if (enabled[0]) {
                val theme = stheme.getValueAsString()
                val mfs = MapsForgeSource(theme)
                return CacheOnlySource(mfs)
            }
            return MAPNIK
        }
    }
}
