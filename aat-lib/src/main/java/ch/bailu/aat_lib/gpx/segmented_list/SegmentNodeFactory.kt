package ch.bailu.aat_lib.gpx.segmented_list

import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.GpxSegmentNode
import ch.bailu.aat_lib.gpx.linked_list.Node
import javax.annotation.Nonnull

abstract class SegmentNodeFactory {
    abstract fun createMarker(n: Node): SegmentNode
    abstract fun createSegment(n: Node, m: SegmentNode): SegmentNode

    companion object {
        val GPX_SEGMENT_FACTORY: SegmentNodeFactory = object : SegmentNodeFactory() {
            override fun createMarker(@Nonnull n: Node): SegmentNode {
                return GpxSegmentNode(n as GpxPointNode)
            }

            override fun createSegment(@Nonnull n: Node, @Nonnull m: SegmentNode): SegmentNode {
                return GpxSegmentNode(n as GpxPointNode, m as GpxSegmentNode)
            }
        }
    }
}
