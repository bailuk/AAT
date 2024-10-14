package ch.bailu.aat_lib.service.cache.gpx

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.gpx.GpxPoint
import ch.bailu.aat_lib.gpx.GpxPointNode
import ch.bailu.aat_lib.gpx.interfaces.GpxType
import ch.bailu.aat_lib.logger.AppLog.e
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.aat_lib.service.editor.GpxEditor
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.xml.writer.GpxListWriter
import ch.bailu.foc.Foc

class ObjGpxEditable(_id: String, private val _file: Foc, sc: AppContext) : ObjGpx(_id) {
    private var currentHandle = NULL
    val editor: GpxListEditor

    init {
        editor = GpxListEditor(sc.broadcaster)
        sc.services.getCacheService().addToBroadcaster(this)
    }

    override fun onInsert(appContext: AppContext) {
        val handle = appContext.services.getCacheService().getObject(_file.path, ObjGpxStatic.Factory())
        currentHandle = if (handle is ObjGpx) {
            handle
        } else {
            NULL
        }
        editor.loadIntoEditor(currentHandle.getGpxList())
    }

    override fun onRemove(appContext: AppContext) {
        currentHandle.free()
        currentHandle = NULL
    }

    override fun isReadyAndLoaded(): Boolean {
        return currentHandle.isReadyAndLoaded()
    }

    override fun getSize(): Long {
        return MIN_SIZE.toLong()
    }

    override fun onDownloaded(id: String, url: String, appContext: AppContext) {}
    override fun onChanged(id: String, appContext: AppContext) {
        if (id == _file.path) {
            editor.loadIntoEditor(currentHandle.getGpxList())
        }
    }

    inner class GpxListEditor(private val broadcaster: Broadcaster) : GpxInformation(),
        EditorInterface {
        private var editor = GpxEditor(GpxList.NULL_ROUTE)
        private var modified = false
        fun loadIntoEditor(list: GpxList?) {
            editor = GpxEditor(list)
            modified = false
            modified(false)
        }

        override fun setType(type: GpxType) {
            editor.setType(type)
            modified(true)
        }

        override fun remove() {
            editor.unlinkSelectedNode()
            modified(true)
        }

        override fun add(point: GpxPoint) {
            editor.insertNode(point)
            modified(true)
        }

        override fun up() {
            editor.moveSelectedUp()
            modified(true)
        }

        override fun down() {
            editor.moveSelectedDown()
            modified(true)
        }

        override fun clear() {
            editor.clear()
            modified(true)
        }

        override fun undo() {
            if (editor.undo()) modified(true)
        }

        override fun redo() {
            if (editor.redo()) modified(true)
        }

        private fun modified(m: Boolean) {
            modified = modified || m
            setVisibleTrackPoint(editor.selectedPoint)
            setVisibleTrackSegment(editor.list.getDelta())
            broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, getID())
        }

        override fun isModified(): Boolean {
            return modified
        }

        override fun select(point: GpxPointNode) {
            editor.select(point, editor.list)
            modified(false)
        }

        override fun save() {
            try {
                GpxListWriter(editor.list, getFile()).close()
                modified = false
                broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_ONDISK, getFile()
                    .toString(), getID()
                )
            } catch (e: Exception) {
                e(this, e)
            }
        }

        override fun unload() {
            loadIntoEditor(currentHandle.getGpxList())
            modified =false
            broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_ONDISK, getFile()
                .toString(), getID()
            )
        }

        override fun inverse() {
            editor.inverse()
            modified(true)
        }

        override fun attach(toAttach: GpxList) {
            editor.attach(toAttach)
            modified(true)
        }

        override fun fix() {
            editor.fix()
            modified(true)
        }

        override fun simplify() {
            editor.simplify()
            modified(true)
        }

        override fun cutPreceding() {
            editor.cutPreceding()
            modified(true)
        }

        override fun cutRemaining() {
            editor.cutRemaining()
            modified(true)
        }

        override fun saveTo(path: Foc) {
            val prefix = AppDirectory.parsePrefix(getFile())
            try {
                val file = AppDirectory.generateUniqueFilePath(
                    path,
                    prefix,
                    AppDirectory.GPX_EXTENSION
                )
                GpxListWriter(editor.list, file).close()
                broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    file.path, getID()
                )
            } catch (e: Exception) {
                e(this, e)
            }
        }

        override fun getSelected(): GpxPointNode {
            return editor.selectedPoint
        }

        override fun getGpxList(): GpxList {
            return editor.list
        }

        override fun getFile(): Foc {
            return _file
        }

        override fun getLoaded(): Boolean {
            return true
        }

        override fun getBoundingBox(): BoundingBoxE6 {
            return editor.list.getDelta().getBoundingBox()
        }
    }

    class Factory(private val file: Foc) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjGpxEditable(id, file, appContext)
        }
    }

    override fun getGpxList(): GpxList {
        return editor.getGpxList()
    }

    companion object {
        fun getVirtualID(file: Foc): String {
            return getVirtualID(file.path)
        }

        private fun getVirtualID(cID: String): String {
            return ObjGpxEditable::class.java.simpleName + cID
        }
    }
}
