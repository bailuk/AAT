package ch.bailu.aat_lib.gpx.segmented_list

import ch.bailu.aat_lib.gpx.linked_list.Node

class SegmentedList(private val factory: SegmentNodeFactory) {
    val segmentList = ch.bailu.aat_lib.gpx.linked_list.List()
    val markerList = ch.bailu.aat_lib.gpx.linked_list.List()
    val pointList = ch.bailu.aat_lib.gpx.linked_list.List()

    fun appendToCurrentSegment(n: Node) {
        pointList.append(n)
        addMarker(n)
        val lastMarker = markerList.last
        val lastSegment = segmentList.last

        if (lastMarker is SegmentNode) {
            lastMarker.update(n)
        }

        if (lastSegment is SegmentNode) {
            lastSegment.update(n)
        }
    }

    private fun addMarker(n: Node) {
        val m = markerList.last
        if (m == null || (m is SegmentNode && m.segmentSize >= DEFAULT_MARKER_SIZE)) {
            markerList.append(factory.createMarker(n))
        }
    }

    fun appendToNewSegment(n: Node) {
        val m = factory.createMarker(n)
        val s = factory.createSegment(n, m)
        markerList.append(m)
        segmentList.append(s)
        pointList.append(n)
        m.update(n)
        s.update(n)
    }

    companion object {
        private const val DEFAULT_MARKER_SIZE = 200
    }
}
