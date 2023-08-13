package ch.bailu.aat_lib.dispatcher

import ch.bailu.aat_lib.service.editor.EditorInterface
import ch.bailu.foc.Foc

interface EditorSourceInterface {
    val isEditing: Boolean
    val editor: EditorInterface
    val file: Foc
    fun edit()
}
