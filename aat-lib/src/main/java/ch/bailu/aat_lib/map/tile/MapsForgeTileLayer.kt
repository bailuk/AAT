package ch.bailu.aat_lib.map.tile

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.TilePainter
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Point
import org.mapsforge.core.model.Rotation
import org.mapsforge.map.layer.Layer
import org.mapsforge.map.model.common.Observer
import org.mapsforge.map.util.LayerUtil
import kotlin.math.roundToInt

class MapsForgeTileLayer(
    private val services: ServicesInterface,
    private val tileProvider: TileProvider,
    private val tilePainter: TilePainter
) : Layer(), MapLayerInterface, Observer {
    private var isAttached = false
    private val paint: Paint = tilePainter.createPaint(tileProvider.source)
    private val rect = Rect()

    init {
        tileProvider.addObserver(this)
    }

    override fun draw(box: BoundingBox, zoom: Byte, c: Canvas, tlp: Point, rotation: Rotation) {
        synchronized(tileProvider) {
            services.insideContext {
                if (detachAttach(zoom.toInt())) {
                    draw(
                        box,
                        zoom,
                        c,
                        tlp,
                        displayModel.tileSize
                    )
                }
            }
        }
    }

    private fun draw(box: BoundingBox, zoom: Byte, canvas: Canvas, tlp: Point, tileSize: Int) {
        val tilePositions = LayerUtil.getTilePositions(box, zoom, tlp, tileSize)
        tileProvider.preload(tilePositions)
        for (tilePosition in tilePositions) {
            val tileBitmap = tileProvider[tilePosition.tile]
            if (tileBitmap != null) {
                val p = tilePosition.point
                rect.left = p.x.roundToInt()
                rect.top = p.y.roundToInt()
                rect.right = rect.left + tileSize
                rect.bottom = rect.top + tileSize
                tilePainter.paint(tileBitmap, canvas, rect, paint)
            }
        }
    }

    override fun onChange() {
        requestRedraw()
    }

    override fun onAttached() {
        synchronized(tileProvider) { isAttached = true }
    }

    override fun onDetached() {
        synchronized(tileProvider) {
            isAttached = false
            tileProvider.onDetached()
        }
    }

    private fun detachAttach(zoom: Int): Boolean {
        if (isVisible && isZoomSupported(zoom) && isAttached) {
            tileProvider.onAttached()
        } else {
            tileProvider.onDetached()
        }
        return tileProvider.isAttached
    }

    private fun isZoomSupported(zoom: Int): Boolean {
        return tileProvider.minimumZoomLevel <= zoom && tileProvider.maximumZoomLevel >= zoom
    }

    fun reDownloadTiles() {
        synchronized(tileProvider) { tileProvider.reDownloadTiles() }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {}
    override fun onTap(tapPos: ch.bailu.aat_lib.util.Point): Boolean {
        return false
    }

    override fun onTap(tapLatLong: LatLong, layerXY: Point, tapXY: Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
}
