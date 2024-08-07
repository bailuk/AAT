package ch.bailu.aat_lib.description

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.BroadcastData
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.ContentSource
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.gpx.GpxFileWrapper
import ch.bailu.aat_lib.gpx.GpxInformation
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

    override fun onPause() {
        appContext.broadcaster.unregister(onFileEdited)
        edit.onPause()
    }

    override fun onResume() {
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
        sendUpdate(edit.infoID, GpxFileWrapper(file, GpxList.NULL_ROUTE))
        edit.edit(file)
        requestUpdate()
    }

    fun editDraft() {
        sendUpdate(edit.infoID, GpxFileWrapper(file, GpxList.NULL_ROUTE))
        edit.editDraft()
        requestUpdate()
    }
}
