package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.gpx.linked_list.Node
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNode

open class GpxSegmentNode : SegmentNode, GpxBigDeltaInterface {
    private val delta = GpxBigDelta(GpxListAttributes.NULL)

    constructor(node: GpxPointNode) : super(node)
    constructor(node: GpxPointNode, marker: GpxSegmentNode) : super(node, marker)

    override fun update(node: Node) {
        val node = node as GpxPointNode

        super.update(node)
        delta.update(node)
    }

    override fun getAcceleration(): Float {
        return delta.getAcceleration()
    }

    override fun getSpeed(): Float {
        return delta.getSpeed()
    }

    override fun getDistance(): Float {
        return delta.getDistance()
    }

    override fun getPause(): Long {
        return delta.getPause()
    }

    override fun getStartTime(): Long {
        return delta.getStartTime()
    }

    override fun getTimeDelta(): Long {
        return delta.getTimeDelta()
    }

    override fun getBoundingBox(): BoundingBoxE6 {
        return delta.getBoundingBox()
    }

    override fun getEndTime(): Long {
        return delta.getEndTime()
    }

    override fun getType(): GpxType {
        return delta.getType()
    }

    override fun getAttributes(): GpxAttributes {
        return delta.getAttributes()
    }
}
