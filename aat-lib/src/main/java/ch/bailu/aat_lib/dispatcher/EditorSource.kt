package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.gpx.information.GpxFileWrapper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.service.editor.EditorHelper
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc

class EditorSource(private val appContext: AppContext) : ContentSource(), EditorSourceInterface {
    private val edit: EditorHelper = EditorHelper(appContext)
    private val onFileEdited = BroadcastReceiver { args ->
        if (BroadcastData.has(args, edit.vID)) {
            requestUpdate()
        }
    }

    override fun requestUpdate() {
        sendUpdate(edit.infoID, edit.info)
    }

    override fun onPauseWithService() {
        appContext.broadcaster.unregister(onFileEdited)
        edit.onPause()
    }
    override fun onDestroy() {}

    override fun onResumeWithService() {
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileEdited)
        edit.onResume()
    }

    override val isEditing = true

    override val editor: EditorInterface
        get() =  edit.editor

    override fun getIID(): Int {
        return edit.infoID
    }

    override fun getInfo(): GpxInformation {
        return edit.info
    }

    override val file: Foc
        get() = edit.file


    override fun edit() {}
    fun edit(file: Foc) {
        sendUpdate(edit.infoID, GpxFileWrapper(file, GpxList.NULL_ROUTE)) // TODO remove
        edit.edit(file)
        requestUpdate()
    }

    @Deprecated("Editor should be able to edit any file, use edit(file: Foc)")
    fun editDraft() {
        sendUpdate(edit.infoID, GpxFileWrapper(file, GpxList.NULL_ROUTE))
        edit.editDraft()
        requestUpdate()
    }
}
