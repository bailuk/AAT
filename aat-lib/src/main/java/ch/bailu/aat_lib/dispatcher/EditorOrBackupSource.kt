package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.usage.UsageTracker
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
import ch.bailu.aat_lib.gpx.information.GpxFileWrapper
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc


class EditorOrBackupSource(appContext: AppContext, source: SourceInterface) :
    EditorSourceInterface, SourceInterface {
    private val editorSource: EditorSource = EditorSource(appContext, UsageTrackerAlwaysEnabled())
    private val backupSource: SourceInterface = source
    override var isEditing = false
        private set

    val isModified: Boolean
        get() = isEditing && editor.isModified()

    fun releaseEditorSave() {
        if (isEditing) {
            editor.save()
            releaseEditorDiscard()
        }
    }

    fun releaseEditorDiscard() {
        if (isEditing) {
            isEditing = false
            editorSource.onPauseWithService()
            backupSource.onResumeWithService()
            requestUpdate()
        }
    }

    override fun onDestroy() {}

    override fun setTarget( target: TargetInterface) {
        editorSource.setTarget(target)
        backupSource.setTarget(target)
    }

    override fun edit() {
        val file = backupSource.info.getFile()
        if (!isEditing) {
            isEditing = true
            editorSource.edit(file)
            editorSource.onResumeWithService()
            backupSource.onPauseWithService()
            requestUpdate()
        }
    }


    override val editor: EditorInterface
        get() =  editorSource.editor

    override fun getIID(): Int {
        return editorSource.getIID()
    }

    override fun getInfo(): GpxInformation {
        return if (isEditing) editorSource.info else backupSource.info
    }

    override fun requestUpdate() {
        if (isEditing) {
            requestNullUpdate(backupSource)
            editorSource.requestUpdate()
        } else {
            requestNullUpdate(editorSource)
            backupSource.requestUpdate()
        }
    }

    override fun onPauseWithService() {
        if (isEditing) editorSource.onPauseWithService() else backupSource.onPauseWithService()
    }

    override fun onResumeWithService() {
        if (isEditing) {
            requestNullUpdate(backupSource)
            editorSource.onResumeWithService()
        } else {
            requestNullUpdate(editorSource)
            backupSource.onResumeWithService()
        }
    }

    override val file: Foc
        get() =  if (isEditing) editorSource.file else backupSource.info.getFile()

    private fun requestNullUpdate(source: SourceInterface) {
        editorSource.sendUpdate(source.getIID(), GpxFileWrapper(file, GpxList.NULL_ROUTE))
    }
}
