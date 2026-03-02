package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.GpxDeltaHelper.getSpeed
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.linked_list.Node

/** A non-first point in a segment; computes distance, speed, acceleration, and time delta relative to its predecessor. */
class GpxPointLinkedNode(tp: GpxPoint, at: GpxAttributes) : GpxPointNode(tp, at) {
    private var distance = 0f //4byte

    override var previous: Node? = null
        set(node) {
            field = node
            if (node is GpxPointInterface) {
                distance = GpxDeltaHelper.getDistance(node, this)
            }
        }

    override fun getAcceleration(): Float {
        val node = previous
        if (node is GpxDeltaPointInterface) {
            return GpxDeltaHelper.getAcceleration(node, this)
        }
        return 0f
    }

    override fun getDistance(): Float {
        return distance
    }

    override fun getSpeed(): Float {
        val node = previous
        if (node is GpxPointInterface) {
            return getSpeed(
                distance,
                GpxDeltaHelper.getTimeDeltaSI(node, this)
            )
        }
        return 0f
    }

    override fun getTimeDelta(): Long {
        val node = previous
        if (node is GpxPointInterface) {
            return GpxDeltaHelper.getTimeDeltaMilli(node, this)
        }
        return 0
    }

    companion object {
        const val SIZE_IN_BYTES: Long = 4
    }
}
