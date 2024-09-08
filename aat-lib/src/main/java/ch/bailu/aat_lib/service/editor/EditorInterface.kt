package ch.bailu.aat_lib.service.editor

import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.foc.Foc

interface EditorInterface {
    fun save()
    fun unload()
    fun setType(type: GpxType)
    fun remove()
    fun add(point: GpxPoint)
    fun up()
    fun down()
    fun isModified(): Boolean

    fun getSelected(): GpxPointNode?
    fun select(p: GpxPointNode)

    fun saveTo(path: Foc)
    fun clear()
    fun redo()
    fun undo()

    fun inverse()
    fun attach(toAttach: GpxList)
    fun fix()
    fun simplify()
    fun cutPreceding()
    fun cutRemaining()

    companion object {
        val NULL: EditorInterface = object : EditorInterface {
            override fun save() {}

            override fun unload() {}

            override fun setType(type: GpxType) {}

            override fun remove() {}

            override fun add(point: GpxPoint) {}

            override fun up() {}

            override fun down() {}

            override fun isModified(): Boolean {
                return false
            }

            override fun getSelected(): GpxPointNode? {
                return null
            }

            override fun select(p: GpxPointNode) {}

            override fun saveTo(path: Foc) {}

            override fun clear() {}

            override fun redo() {}

            override fun undo() {}

            override fun inverse() {}

            override fun attach(file: GpxList) {}

            override fun fix() {}

            override fun simplify() {}

            override fun cutPreceding() {}

            override fun cutRemaining() {}
        }
    }
}
