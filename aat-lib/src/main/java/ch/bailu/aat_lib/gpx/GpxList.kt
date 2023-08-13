package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes
import ch.bailu.aat_lib.gpx.interfaces.GpxBigDeltaInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.gpx.linked_list.List
import ch.bailu.aat_lib.gpx.segmented_list.SegmentNodeFactory.Companion.GPX_SEGMENT_FACTORY
import ch.bailu.aat_lib.gpx.segmented_list.SegmentedList

class GpxList(type: GpxType, attr: GpxListAttributes) {
    companion object {
        @JvmField
        val NULL_TRACK = GpxList(GpxType.TRACK, GpxListAttributes.NULL)

        @JvmField
        val NULL_ROUTE = GpxList(GpxType.ROUTE, GpxListAttributes.NULL)

    }

    private val list = SegmentedList(GPX_SEGMENT_FACTORY)
    private val delta: GpxBigDelta

    init {
        delta = GpxBigDelta(attr)
        delta.setType(type)
    }

    fun appendToCurrentSegment(tp: GpxPoint, at: GpxAttributes) {
        if (list.segmentList.size() == 0) {
            appendToNewSegment(tp, at)
        } else {
            val n = GpxPointLinkedNode(tp, at)
            list.appendToCurrentSegment(n)
            delta.update(n)
        }
    }

    fun appendToNewSegment(tp: GpxPoint, at: GpxAttributes) {
        val n: GpxPointNode = GpxPointFirstNode(tp, at)
        list.appendToNewSegment(n)
        delta.updateWithPause(n)
    }

    val pointList: List
        get() = list.pointList
    val segmentList: List
        get() = list.segmentList
    val markerList: List
        get() = list.markerList

    fun getDelta(): GpxBigDeltaInterface {
        return delta
    }

    fun setType(type: GpxType?) {
        delta.setType(type)
    }


}
