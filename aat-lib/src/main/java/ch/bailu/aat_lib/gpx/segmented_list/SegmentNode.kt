package ch.bailu.aat_lib.gpx.segmented_list

import ch.bailu.aat_lib.gpx.linked_list.Node

open class SegmentNode(val firstNode: Node, val marker: SegmentNode? = null) : Node() {
    var segmentSize = 0
        private set

    open fun update(node: Node) {
        segmentSize++
    }
}
