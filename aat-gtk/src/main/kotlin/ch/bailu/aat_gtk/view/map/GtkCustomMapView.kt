package ch.bailu.aat_gtk.view.map

import ch.bailu.aat_lib.app.AppGraphicFactory
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.map.*
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.map.layer.MapPositionLayer
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Limit
import ch.bailu.gtk.cairo.Context
import org.mapsforge.core.model.*
import org.mapsforge.core.util.LatLongUtils
import org.mapsforge.core.util.Parameters
import org.mapsforge.map.datastore.MapDataStore
import org.mapsforge.map.datastore.MultiMapDataStore
import org.mapsforge.map.gtk.graphics.GtkGraphicContext
import org.mapsforge.map.gtk.util.TileCacheUtil
import org.mapsforge.map.gtk.view.MapView
import org.mapsforge.map.layer.Layers
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.debug.TileCoordinatesLayer
import org.mapsforge.map.layer.debug.TileGridLayer
import org.mapsforge.map.layer.download.TileDownloadLayer
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik
import org.mapsforge.map.layer.download.tilesource.TileSource
import org.mapsforge.map.layer.hills.HillsRenderConfig
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.model.IMapViewPosition
import org.mapsforge.map.model.Model
import org.mapsforge.map.model.common.Observer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File
import java.util.*

class GtkCustomMapView (
    private val storage: StorageInterface,
    mapFiles: List<File>,
    dispatcher: DispatcherInterface
) : MapView(), MapViewInterface, OnPreferencesChanged, Attachable {

    private val backgroundContext: GtkMapContext = GtkMapContext(this, this.javaClass.simpleName)
    private val foregroundContext: GtkMapContextForeground
    private val pos: MapPositionLayer
    private val layers = ArrayList<MapLayerInterface>(10)
    private val bounding: BoundingBox


    init {
        foregroundContext = GtkMapContextForeground(
            MapsForgeMetrics(this, AppDensity()), backgroundContext, layers)
        addLayer(backgroundContext)
        mapScaleBar.isVisible = false
        if (SHOW_DEBUG_LAYERS) {
            fpsCounter.isVisible = true
        }
        Parameters.SQUARE_FRAME_BUFFER = false
        bounding = addLayers(this, mapFiles, null)
        pos = MapPositionLayer(mContext, storage, dispatcher)

        add(pos)
        model.mapViewPosition.addObserver(object : Observer {
            private var center: LatLong = model.mapViewPosition.center
            override fun onChange() {
                val newCenter: LatLong = model.mapViewPosition.center
                if (newCenter != center) {
                    center = newCenter
                    pos.onMapCenterChanged(center)
                }
            }
        })
        onAttached()
    }
    fun showMap() {
        val model: Model = model
        val zoomLevel = LatLongUtils.zoomForBounds(
            model.mapViewDimension.dimension,
            bounding,
            model.displayModel.tileSize
        )
        model.mapViewPosition.mapPosition = MapPosition(bounding.centerPoint, zoomLevel)
    }

    override fun frameBounding(boundingBox: BoundingBoxE6) {
        frameBounding(boundingBox.toBoundingBox())
    }

    private fun frameBounding(bounding: BoundingBox) {
        val dimension = model.mapViewDimension.dimension
        if (dimension != null) {
            val zoom: Byte = zoomForBounds(bounding, dimension)
            val position = MapPosition(bounding.centerPoint, zoom)
            model.mapViewPosition.mapPosition = position
        }
    }

    private fun zoomForBounds(bounding: BoundingBox, dimension: Dimension): Byte {
        var zoom: Byte
        zoom =
            if (bounding.minLatitude == 0.0 && bounding.minLongitude == 0.0 && bounding.maxLatitude == 0.0 && bounding.maxLongitude == 0.0) {
                0
            } else {
                LatLongUtils.zoomForBounds(
                    dimension,
                    bounding,
                    model.displayModel.tileSize
                )
            }
        zoom = Math.min(zoom.toInt(), model.mapViewPosition.zoomLevelMax.toInt()).toByte()
        zoom = Math.max(zoom.toInt(), model.mapViewPosition.zoomLevelMin.toInt()).toByte()
        return zoom
    }

    override fun zoomOut() {
        setZoomLevel(
            Limit.clamp(
                model.mapViewPosition.zoomLevel - 1,
                model.mapViewPosition.zoomLevelMin.toInt(),
                model.mapViewPosition.zoomLevelMax.toInt()
            ).toByte()
        )
    }

    override fun zoomIn() {
        setZoomLevel(
            Limit.clamp(
                model.mapViewPosition.zoomLevel + 1,
                model.mapViewPosition.zoomLevelMin.toInt(),
                model.mapViewPosition.zoomLevelMax.toInt()
            ).toByte()
        )
    }

    override fun onResize(width: Int, height: Int) {
        super.onResize(width, height)
        layers.forEach {
            it.onLayout(true, 0,0,width, height)
        }
    }

    override fun requestRedraw() {
        layerManager.redrawLayers()
    }

    override fun add(l: MapLayerInterface) {
        addLayer(LayerWrapper(backgroundContext, l))
        layers.add(l)
    }

    override fun getMContext(): MapContext {
        return backgroundContext
    }

    override fun reDownloadTiles() {}
    override fun getMapViewPosition(): IMapViewPosition {
        return model.mapViewPosition
    }

    override fun onDraw(context: Context) {
        val canvas = GtkGraphicContext(context, width, height)
        foregroundContext.dispatchDraw(canvas)
    }

    override fun onAttached() {
        storage.register(this)
        for (layer in layers) {
            layer.onAttached()
        }
    }

    override fun onDetached() {
        storage.unregister(this)
        for (layer in layers) {
            layer.onDetached()
        }
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        for (layer in layers) {
            layer.onPreferencesChanged(storage, key)
        }
    }

    companion object {
        private const val SHOW_DEBUG_LAYERS = false
        private const val SHOW_RASTER_MAP = false
        private fun addLayers(
            mapView: MapView,
            mapFiles: List<File>,
            hillsRenderConfig: HillsRenderConfig?
        ): BoundingBox {
            val layers: Layers = mapView.layerManager.layers
            val tileSize = 256

            // Tile cache
            val tileCache: TileCache = TileCacheUtil.createTileCache(
                tileSize,
                mapView.model.frameBufferModel.overdrawFactor,
                1024,
                File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString())
            )
            val boundingBox: BoundingBox
            if (SHOW_RASTER_MAP) {
                // Raster
                mapView.model.displayModel.setFixedTileSize(tileSize)
                val tileSource = OpenStreetMapMapnik.INSTANCE
                tileSource.userAgent = "mapsforge-samples-awt"
                val tileDownloadLayer = createTileDownloadLayer(
                    tileCache,
                    mapView.getModel().mapViewPosition,
                    tileSource
                )
                layers.add(tileDownloadLayer)
                tileDownloadLayer.start()
                mapView.setZoomLevelMin(tileSource.zoomLevelMin)
                mapView.setZoomLevelMax(tileSource.zoomLevelMax)
                boundingBox = BoundingBox(
                    LatLongUtils.LATITUDE_MIN,
                    LatLongUtils.LONGITUDE_MIN,
                    LatLongUtils.LATITUDE_MAX,
                    LatLongUtils.LONGITUDE_MAX
                )
            } else {
                // Vector
                mapView.model.displayModel.setFixedTileSize(tileSize)
                val mapDataStore = MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL)
                for (file in mapFiles) {
                    mapDataStore.addMapDataStore(MapFile(file), false, false)
                }
                val tileRendererLayer = createTileRendererLayer(
                    tileCache,
                    mapDataStore,
                    mapView.model.mapViewPosition,
                    hillsRenderConfig
                )
                layers.add(tileRendererLayer)
                boundingBox = mapDataStore.boundingBox()
            }

            // Debug
            if (SHOW_DEBUG_LAYERS) {
                layers.add(
                    TileGridLayer(
                        AppGraphicFactory.instance(),
                        mapView.model.displayModel
                    )
                )
                layers.add(
                    TileCoordinatesLayer(
                        AppGraphicFactory.instance(),
                        mapView.model.displayModel
                    )
                )
            }
            return boundingBox
        }

        private fun createTileDownloadLayer(
            tileCache: TileCache,
            mapViewPosition: IMapViewPosition,
            tileSource: TileSource
        ): TileDownloadLayer {
            return object : TileDownloadLayer(
                tileCache,
                mapViewPosition,
                tileSource,
                AppGraphicFactory.instance()
            ) {
                override fun onTap(tapLatLong: LatLong, layerXY: Point?, tapXY: Point): Boolean {
                    println("Tap on: $tapLatLong")
                    return true
                }
            }
        }

        private fun createTileRendererLayer(
            tileCache: TileCache,
            mapDataStore: MapDataStore,
            mapViewPosition: IMapViewPosition,
            hillsRenderConfig: HillsRenderConfig?
        ): TileRendererLayer {
            val tileRendererLayer: TileRendererLayer = object : TileRendererLayer(
                tileCache,
                mapDataStore,
                mapViewPosition,
                false,
                true,
                false,
                AppGraphicFactory.instance(),
                hillsRenderConfig
            ) {
                override fun onTap(tapLatLong: LatLong, layerXY: Point?, tapXY: Point): Boolean {
                    println("Tap on: $tapLatLong")
                    return true
                }
            }
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT)
            return tileRendererLayer
        }
    }

}