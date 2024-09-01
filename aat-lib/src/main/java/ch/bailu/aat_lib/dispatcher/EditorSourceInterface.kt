package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc

interface EditorSourceInterface {
    @Deprecated("Editor should be able to edit any file, use edit(file: Foc)")
    val isEditing: Boolean
    val editor: EditorInterface
    val file: Foc

    @Deprecated("Editor should be able to edit any file, use edit(file: Foc)")
    fun edit()
}
