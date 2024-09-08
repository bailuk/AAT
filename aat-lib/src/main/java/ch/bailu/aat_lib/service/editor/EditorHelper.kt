package ch.bailu.aat_lib.service.editor

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.GpxInformationProvider
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.service.cache.ObjNull
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxEditable
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc

class EditorHelper(private val appContext: AppContext) :
    GpxInformationProvider {
    private var handle = ObjNull.NULL
    var infoID = 0
        private set
    var file: Foc = Foc.FOC_NULL
        private set
    var vID: String = ""
        private set

    init {
        edit(draft, InfoID.EDITOR_DRAFT)
    }

    @Deprecated("Editor should be able to edit any file, use edit(file: Foc)")
    fun editDraft() {
        edit(draft, InfoID.EDITOR_DRAFT)
        onResume()
    }

    fun edit(file: Foc) {
        edit(file, InfoID.EDITOR_OVERLAY)
        onResume()
    }

    private fun edit(file: Foc, iid: Int) {
        infoID = iid
        this.file = file
        vID = ObjGpxEditable.getVirtualID(file)
    }

    fun onResume() {
        val newHandle = appContext.services.getCacheService().getObject(
            vID,
            ObjGpxEditable.Factory(file)
        )
        handle.free()
        handle = newHandle
    }

    fun onPause() {
        if (infoID == InfoID.EDITOR_DRAFT) save()
        handle.free()
        handle = ObjGpx.NULL
    }

    override fun getInfo(): GpxInformation {
        return if (handle is ObjGpxEditable) {
            (handle as ObjGpxEditable).editor
        } else GpxInformation.NULL
    }

    val editor: EditorInterface
        get() = if (handle is ObjGpxEditable) {
            (handle as ObjGpxEditable).editor
        } else EditorInterface.NULL

    fun save() {
        val editor = editor
        if (editor.isModified()) {
            editor.save()
        }
    }

    private val draft: Foc
        get() = AppDirectory.getEditorDraft(appContext.dataDirectory)
}
