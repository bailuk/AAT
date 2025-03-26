package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.coordinates.BoundingBoxE6.Companion.doOverlap
import ch.bailu.aat_lib.coordinates.LatLongE6
import ch.bailu.aat_lib.coordinates.LatLongE6.Companion.toD
import ch.bailu.aat_lib.coordinates.LatLongE6.Companion.toLatLong
import ch.bailu.aat_lib.coordinates.LatLongInterface
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.model.BoundingBox
import org.mapsforge.core.model.Dimension
import org.mapsforge.core.model.LatLong
import org.mapsforge.core.model.Point
import org.mapsforge.core.model.Rotation
import org.mapsforge.core.util.MercatorProjection
import org.mapsforge.map.util.MapPositionUtil
import org.mapsforge.map.view.MapView

class MapsForgeMetrics(private val mapView: MapView, private val density: AppDensity) : MapMetrics {
    private var tl: Point? = null
    private var zoom: Byte = 0
    private var dim: Dimension? = null
    private var center: ch.bailu.aat_lib.util.Point? = null
    private var bounding: BoundingBox
    private val distances = MapDistances()
    private var tileSize: Int

    init {
        bounding = mapView.boundingBox
        tileSize = mapView.model.displayModel.tileSize
    }

    fun init(b: BoundingBox, z: Byte, d: Dimension, p: Point) {
        tileSize = mapView.model.displayModel.tileSize
        dim = d
        bounding = b
        tl = p
        zoom = z

        val dimension = dim
        if (dimension is Dimension) {
            distances.init(bounding, dimension)
            center = ch.bailu.aat_lib.util.Point(dimension.width / 2, dimension.height / 2)
        }
    }

    fun init(d: Dimension) {
        val pos = mapView.model.mapViewPosition.mapPosition
        init(
            MapPositionUtil.getBoundingBox(pos, Rotation.NULL_ROTATION, tileSize, d, 0f, 0f),
            mapView.model.mapViewPosition.zoomLevel,
            d,
            MapPositionUtil.getTopLeftPoint(pos, d, tileSize)
        )
    }

    override fun getDensity(): AppDensity {
        return density
    }

    override fun getLeft(): Int {
        return 0
    }

    override fun getRight(): Int {
        return dim?.width ?: 0
    }

    override fun getTop(): Int {
        return 0
    }

    override fun getBottom(): Int {
        return dim?.height ?: 0
    }

    override fun getWidth(): Int {
        return dim?.width ?: 0
    }

    override fun getHeight(): Int {
        return dim?.height ?: 0
    }

    override fun pixelToDistance(pixel: Int): Float {
        return distances.toDistance(pixel.toFloat())
    }

    override fun distanceToPixel(meter: Float): Int {
        return distances.toPixel(meter).toInt()
    }

    override fun getShortDistance(): Int {
        return distances.shortDistance.toInt()
    }

    override fun getCenterPixel(): ch.bailu.aat_lib.util.Point {
        return center ?: return ch.bailu.aat_lib.util.Point(0,0)
    }

    override fun isVisible(box: BoundingBoxE6): Boolean {
        return doOverlap(box, BoundingBoxE6(bounding))
    }

    override fun isVisible(point: LatLongInterface): Boolean {
        return bounding.contains(
            toD(point.getLatitudeE6()),
            toD(point.getLongitudeE6())
        )
    }

    override fun toMapPixels(box: BoundingBoxE6): Rect {
        val rect = Rect()
        val tl = toPixel(LatLongE6(box.latNorthE6, box.lonWestE6).toLatLong())
        val br = toPixel(LatLongE6(box.latSouthE6, box.lonEastE6).toLatLong())
        rect.left = tl.x
        rect.right = br.x
        rect.bottom = br.y
        rect.top = tl.y
        return rect
    }

    override fun toPixel(tp: LatLongInterface): ch.bailu.aat_lib.util.Point {
        return toPixel(toLatLong(tp))
    }

    override fun toPixel(p: LatLong): ch.bailu.aat_lib.util.Point {
        val y = MercatorProjection.latitudeToPixelY(p.getLatitude(), zoom, tileSize)
        val x = MercatorProjection.longitudeToPixelX(p.getLongitude(), zoom, tileSize)
        tl?.apply {
            return ch.bailu.aat_lib.util.Point(x - this.x, y - this.y)
        }

        return ch.bailu.aat_lib.util.Point(0, 0)
    }

    override fun fromPixel(x: Int, y: Int): LatLong? {
        return mapView.mapViewProjection.fromPixels(x.toDouble(), y.toDouble())
    }

    override fun getBoundingBox(): BoundingBox {
        return bounding
    }

    override fun getZoomLevel(): Int {
        return zoom.toInt()
    }
}
