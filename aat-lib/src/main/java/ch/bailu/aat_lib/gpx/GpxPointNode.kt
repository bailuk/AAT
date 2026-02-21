package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.Keys.Companion.toIndex
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface
import ch.bailu.aat_lib.gpx.linked_list.Node
import javax.annotation.Nonnull

abstract class GpxPointNode(@JvmField val point: GpxPoint, private val attributes: GpxAttributes) : Node(),
    GpxDeltaPointInterface {
    override fun getAltitude(): Float {
        return point.getAltitude()
    }

    override fun getLatitude(): Double {
        return point.getLatitude()
    }

    override fun getLongitude(): Double {
        return point.getLongitude()
    }

    override fun getTimeStamp(): Long {
        return point.getTimeStamp()
    }

    override fun getLatitudeE6(): Int {
        return point.getLatitudeE6()
    }

    override fun getLongitudeE6(): Int {
        return point.getLongitudeE6()
    }

    @Nonnull
    override fun toString(): String {
        return attributes.toString()
    }

    override fun getAttributes(): GpxAttributes {
        return attributes
    }

    fun setAltitude(e: Double) {
        point.setAltitude(e.toFloat())
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        val box = BoundingBoxE6()

        if (attributes.hasKey(BOUNDING_KEY)) {
            box.add(attributes[BOUNDING_KEY])
        }
        box.add(this.point)

        return box
    }

    companion object {
        private val BOUNDING_KEY = toIndex("boundingbox")
    }
}
