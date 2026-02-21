package ch.bailu.aat_lib.service.editor

import kotlin.math.min

class EditorRing(s: NodeEditor) {
    private val ring = ArrayList<NodeEditor>(RING_SIZE)
    private var index = 0
    private var undoable = 0
    private var redoable = 0

    init {
        ring.add(s)
    }

    fun add(s: NodeEditor) {
        index++
        if (index >= RING_SIZE) index = 0

        undoable = min(undoable + 1, RING_SIZE)
        redoable = 0

        if (index < ring.size) {
            ring[index] = s
        } else {
            ring.add(s)
        }
    }

    fun set(s: NodeEditor) {
        ring[index] = s
    }

    fun get(): NodeEditor {
        return ring[index]
    }

    fun undo(): Boolean {
        if (undoable > 0) {
            undoable--
            redoable++
            index--
            if (index < 0) index = ring.size - 1
            return true
        }
        return false
    }

    fun redo(): Boolean {
        if (redoable > 0) {
            undoable++
            redoable--
            index = (index + 1) % ring.size
            return true
        }
        return false
     }

    companion object {
        const val RING_SIZE = 10
    }
}
