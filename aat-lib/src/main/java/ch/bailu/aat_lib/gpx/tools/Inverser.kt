package ch.bailu.aat_lib.gpx.tools

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxListArray
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointFirstNode
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.attributes.GpxListAttributes

class Inverser(track: GpxList) {
    val newList: GpxList = GpxList(
        track.getDelta().getType(),
        GpxListAttributes.NULL
    )

    init {
        val list = GpxListArray(track)
        val listInverse = GpxListArray(track)
        if (list.size() > 0) {
            var indexInverse = list.size() - 1
            var index = 0
            while (indexInverse >= 0) {
                listInverse.index = indexInverse
                list.index = index
                val point = list.get()
                val pointInverse = listInverse.get()
                val pointNew = GpxPoint(
                    pointInverse, pointInverse.getAltitude().toFloat(),
                    point.getTimeStamp()
                )
                if (isLastInSegment(pointInverse)) newList.appendToNewSegment(
                    pointNew,
                    pointInverse.getAttributes()
                ) else newList.appendToCurrentSegment(pointNew, pointInverse.getAttributes())
                index++
                indexInverse--
            }
        }
    }

    private fun isLastInSegment(point: GpxPointNode): Boolean {
        return point.next == null || point.next is GpxPointFirstNode
    }
}
