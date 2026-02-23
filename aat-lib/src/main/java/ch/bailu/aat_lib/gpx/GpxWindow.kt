package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaInterface
import javax.annotation.Nonnull

/** Sliding window over a linked list of {@link GpxPointNode}s, trimming from the front when a subclass-defined limit is exceeded. */
abstract class GpxWindow(private var first: GpxPointNode?) : GpxDeltaInterface {
    private var last: GpxPointNode?

    private var distance = 0f
    private var timeDeltaMillis: Long = 0

    init {
        last = first
    }

    fun forward(node: GpxPointNode) {
        last = node

        distance += node.getDistance()
        timeDeltaMillis += node.getTimeDelta()

        trim()
    }

    private fun trim() {
        while (overLimit() && first !== last && first!!.next is GpxPointNode) {
            timeDeltaMillis -= first!!.getTimeDelta()
            distance -= first!!.getDistance()

            first = first!!.next as GpxPointNode?
        }
    }

    protected abstract fun overLimit(): Boolean

    override fun getDistance(): Float {
        return distance
    }


    override fun getSpeed(): Float {
        if (timeDeltaMillis > 0) {
            return (distance * 1000f) / timeDeltaMillis
        } else {
            return 0f
        }
    }

    override fun getAcceleration(): Float {
        return 0f
    }

    override fun getTimeDelta(): Long {
        return timeDeltaMillis
    }

    @Nonnull
    override fun getBoundingBox(): BoundingBoxE6 {
        return BoundingBoxE6.NULL_BOX
    }
}
