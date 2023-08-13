package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.gpx.GpxFileWrapper
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc
import javax.annotation.Nonnull

class EditorOrBackupSource(appContext: AppContext, source: ContentSourceInterface) :
    EditorSourceInterface, ContentSourceInterface {
    private val editorSource: EditorSource
    private val backupSource: ContentSourceInterface
    override var isEditing = false
        private set

    init {
        editorSource = EditorSource(appContext)
        backupSource = source
    }

    val isModified: Boolean
        get() = isEditing && editor.isModified

    fun releaseEditorSave() {
        if (isEditing) {
            editor.save()
            releaseEditorDiscard()
        }
    }

    fun releaseEditorDiscard() {
        if (isEditing) {
            isEditing = false
            editorSource.onPause()
            backupSource.onResume()
            requestUpdate()
        }
    }

    override fun setTarget(@Nonnull target: OnContentUpdatedInterface) {
        editorSource.setTarget(target)
        backupSource.setTarget(target)
    }

    override fun edit() {
        val file = backupSource.info.file
        if (file != null && !isEditing) {
            isEditing = true
            editorSource.edit(file)
            editorSource.onResume()
            backupSource.onPause()
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

    override fun onPause() {
        if (isEditing) editorSource.onPause() else backupSource.onPause()
    }

    override fun onResume() {
        if (isEditing) {
            requestNullUpdate(backupSource)
            editorSource.onResume()
        } else {
            requestNullUpdate(editorSource)
            backupSource.onResume()
        }
    }

    override val file: Foc
        get() =  if (isEditing) editorSource.file else backupSource.info.file

    private fun requestNullUpdate(source: ContentSourceInterface) {
        editorSource.sendUpdate(source.getIID(), GpxFileWrapper(file, GpxList.NULL_ROUTE))
    }
}
