package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes

open class GpxPointFirstNode(point: GpxPoint, attributes: GpxAttributes) : GpxPointNode(point, attributes) {
    override fun getSpeed(): Float {
        return 0f
    }

    override fun getAcceleration(): Float {
        return 0f
    }

    override fun getDistance(): Float {
        return 0f
    }

    override fun getTimeDelta(): Long {
        return 0
    }
}
