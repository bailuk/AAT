package ch.bailu.aat_lib.service.editor

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxPointInterface
import ch.bailu.aat_lib.gpx.interfaces.GpxType

class GpxEditor(list: GpxList) {
    private val ring: EditorRing = if (list.pointList.size() > 0) {
        EditorRing(
            (NodeEditor(
                (list.pointList.first as GpxPointNode?)!!,
                list
            ))
        )
    } else {
        EditorRing(NodeEditor())
    }

    fun select(point: GpxPointNode, list: GpxList) {
        ring.set(NodeEditor(point, list))
    }

    fun clear() {
        ring.add(NodeEditor())
    }

    fun unlinkSelectedNode() {
        ring.add(ring.get().unlink())
    }

    fun insertNode(point: GpxPointInterface) {
        ring.add(ring.get().insert(point))
    }

    fun moveSelectedUp() {
        val point: GpxPointInterface = ring.get().point

        ring.add(ring.get().unlink())
        ring.set(ring.get().previous())
        ring.set(ring.get().insert(point))
    }

    fun moveSelectedDown() {
        val point: GpxPointInterface = ring.get().point

        ring.add(ring.get().unlink())
        ring.set(ring.get().next())
        ring.set(ring.get().insert(point))
    }

    val list: GpxList
        get() = ring.get().list

    val selectedPoint: GpxPointNode
        get() = ring.get().point

    fun setType(type: GpxType) {
        ring.get().list.setType(type)
    }

    fun undo(): Boolean {
        return ring.undo()
    }

    fun redo(): Boolean {
        return ring.redo()
    }

    fun simplify() {
        ring.add(ring.get().simplify())
    }

    fun fix() {
        ring.add(ring.get().fix())
    }

    fun inverse() {
        ring.add(ring.get().inverse())
    }

    fun attach(toAttach: GpxList) {
        ring.add(ring.get().attach(toAttach))
    }

    fun cutPreceding() {
        ring.add(ring.get().cutPreceding())
    }

    fun cutRemaining() {
        ring.add(ring.get().cutRemaining())
    }
}
