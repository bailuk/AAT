package ch.bailu.aat.map.mapsforge

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import ch.bailu.aat.map.MapDensity
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.util.OldAppBroadcaster
import ch.bailu.aat.util.graphic.AndroidSyncTileBitmap
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
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
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.util.LayerUtil
import org.mapsforge.map.util.MapPositionUtil
import org.mapsforge.map.view.FrameBuffer
import org.mapsforge.map.view.FrameBufferHA3

class MapsForgePreview(context: Context, appContext: AppContext, info: GpxInformation, out: Foc):
    MapsForgeViewBase(appContext, context, MapsForgePreview::class.java.simpleName, MapDensity()),
    MapPreviewInterface {

    private val imageFile: Foc
    private val provider: TileProvider
    private val mapPosition: MapPosition
    private val bounding: BoundingBox
    private val tlPoint: Point

    init {
        layout(0, 0, BITMAP_SIZE, BITMAP_SIZE)
        model.mapViewDimension.dimension = DIM
        imageFile = out
        provider = TileProvider(appContext, getSource(SolidRenderTheme(appContext.mapDirectory, appContext)))

        val tileLayer = MapsForgeTileLayer(appContext.services, provider, appContext.tilePainter)
        add(tileLayer, tileLayer)

        val gpxLayer = GpxDynLayer(Storage(getContext()), mContext, appContext.services)
        add(gpxLayer)
        attachLayers()
        gpxLayer.onContentUpdated(InfoID.FILEVIEW, info)
        frameBounding(info.gpxList.delta.boundingBox)
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
            AndroidGraphicFactory.INSTANCE
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

    private fun generateBitmap(): AndroidSyncTileBitmap {
        val bitmap = AndroidSyncTileBitmap()

        bitmap[BITMAP_SIZE] = false
        if (bitmap.androidBitmap != null) {
            val c = bitmap.androidCanvas
            val canvas = AndroidGraphicFactory.createGraphicContext(c)
            bitmap.androidBitmap.eraseColor(Color.BLACK)
            for (layer in layerManager.layers) {
                layer.draw(bounding, mapPosition.zoomLevel, canvas, tlPoint)
            }
        }
        //drawingCanvas.destroy();
        return bitmap
    }

    @SuppressLint("WrongThread")
    override fun generateBitmapFile() {
        val bitmap = generateBitmap()
        try {
            val outStream = imageFile.openW()
            bitmap.androidBitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, outStream)
            outStream.close()
            OldAppBroadcaster.broadcast(
                context,
                AppBroadcaster.FILE_CHANGED_ONDISK,
                imageFile.toString(),
                javaClass.name
            )
        } catch (e: Exception) {
            AppLog.e(context, e)
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
        private const val BITMAP_SIZE = 128
        private val DIM = Dimension(BITMAP_SIZE, BITMAP_SIZE)
        private val MAPNIK: Source = CacheOnlySource(DownloadSource.MAPNIK)

        private fun getSource(stheme: SolidRenderTheme): Source {
            val tiles = SolidMapTileStack(stheme)
            val enabled = tiles.enabledArray
            if (enabled[0]) {
                val theme = stheme.valueAsString
                val mfs = MapsForgeSource(theme)
                return CacheOnlySource(mfs)
            }
            return MAPNIK
        }
    }
}
