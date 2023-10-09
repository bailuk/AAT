package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TilePainter
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Point
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.model.DisplayModel
import org.mapsforge.map.view.MapView

open class MapsForgeTileLayerStack(private val scontext: ServicesInterface) : Layer(),
    MapLayerInterface {
    private val layers = SubLayers()
    private var minZoom = 5
    private var maxZoom = 5
    fun addLayer(tileProvider: TileProvider, tilePainter: TilePainter?) {
        val layer = MapsForgeTileLayer(scontext, tileProvider, tilePainter)
        layer.displayModel = getDisplayModel()
        layers.add(layer)
        tileProvider.addObserver { requestRedraw() }
        maxZoom = Math.max(tileProvider.maximumZoomLevel, maxZoom)
        minZoom = Math.min(tileProvider.minimumZoomLevel, minZoom)
    }

    override fun setDisplayModel(model: DisplayModel) {
        super.setDisplayModel(model)
        layers.setDisplayModel(model)
    }

    fun removeLayers() {
        layers.clear()
        maxZoom = 5
        minZoom = maxZoom
    }

    fun setMapViewZoomLimit(mapView: MapView) {
        mapView.setZoomLevelMin(minZoom.toByte())
        mapView.setZoomLevelMax(maxZoom.toByte())
    }

    override fun draw(box: BoundingBox, zoom: Byte, c: Canvas, tlp: Point) {
        scontext.insideContext { layers.draw(box, zoom, c, tlp) }
    }

    fun reDownloadTiles() {
        layers.reDownloadTiles()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: ch.bailu.aat_lib.util.Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {
        layers.attach()
    }

    override fun onDestroy() {
        layers.clear()
    }

    override fun onDetached() {
        layers.detach()
    }

    private class SubLayers {
        private var isAttached = false
        private val layers = ArrayList<MapsForgeTileLayer>(10)
        @Synchronized
        fun add(l: MapsForgeTileLayer) {
            layers.add(l)
            if (isAttached) l.onAttached()
        }

        @Synchronized
        fun clear() {
            for (c in layers) {
                c.onDetached()
            }
            layers.clear()
        }

        @Synchronized
        fun attach() {
            for (a in layers) a.onAttached()
            isAttached = true
        }

        @Synchronized
        fun detach() {
            for (a in layers) {
                a.onDetached()
            }
            isAttached = false
        }

        @Synchronized
        fun draw(box: BoundingBox?, zoom: Byte, c: Canvas?, tlp: Point?) {
            for (l in layers) {
                l.draw(box, zoom, c, tlp)
            }
        }

        @Synchronized
        fun reDownloadTiles() {
            for (l in layers) {
                l.reDownloadTiles()
            }
        }

        @Synchronized
        fun setDisplayModel(model: DisplayModel?) {
            for (l in layers) {
                l.displayModel = model
            }
        }
    }
}
