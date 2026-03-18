package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.linked_list.Node
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNode

/** Visitor that walks a {@link GpxList}'s hierarchy (list -> segments -> markers -> points) with optional point skipping. */
abstract class GpxListWalker {
    private var increment = 0
    private var count = 0

    fun walkTrack(track: GpxList, inc: Int) {
        increment = inc - 1

        walkTrack(track)
    }

    fun walkTrack(track: GpxList) {
        increment = 0
        if (doList(track)) {
            var segment = track.segmentList.first

            while (segment != null) {
                walkSegment(segment as GpxSegmentNode)
                segment = segment.next
            }
        }
    }

    private fun walkSegment(segment: GpxSegmentNode) {
        if (doSegment(segment)) {
            var count = segment.segmentSize
            var marker: Node? = segment.marker

            while (count > 0) {
                walkMarker((marker as GpxSegmentNode?)!!)

                count -= (marker as SegmentNode).segmentSize
                marker = marker.next
            }
        }
    }

    private fun walkMarker(marker: GpxSegmentNode) {
        if (doMarker(marker)) {
            var count = marker.segmentSize
            var node: Node? = marker.firstNode
            while (count > 0) {
                if (this.count == 0) {
                    this.count = increment
                    if (node is GpxPointNode) {
                        doPoint(node)
                    }
                } else {
                    this.count--
                }

                count--
                node = node?.next
            }
        }
    }

    abstract fun doList(track: GpxList): Boolean
    abstract fun doSegment(segment: GpxSegmentNode): Boolean
    abstract fun doMarker(marker: GpxSegmentNode): Boolean
    abstract fun doPoint(point: GpxPointNode)
}
