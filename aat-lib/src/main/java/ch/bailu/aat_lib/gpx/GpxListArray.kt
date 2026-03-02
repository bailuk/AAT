package ch.bailu.aat_lib.gpx

import ch.bailu.aat_lib.gpx.attributes.GpxAttributesNull
import ch.bailu.aat_lib.gpx.linked_list.Node

/** Index-based random access over a [GpxList]'s linked list of points, caching the current position. */
class GpxListArray(val list: GpxList) {
    var index = 0
        set(i) {
            while (field < i) {
                current = current?.next
                field++
            }
            while (field > i) {
                current = current?.previous
                field--
            }
        }
    private var current: Node?

    init {
        current = list.pointList.first
    }

    operator fun get(i: Int): GpxPointNode {
        index = i
        return get()
    }

    fun get(): GpxPointNode {
        val result = current

        if (result is GpxPointNode) {
            return result
        }
        return GpxPointFirstNode(GpxPoint.NULL, GpxAttributesNull.NULL)
    }

    fun size(): Int {
        return list.pointList.size()
    }
}
