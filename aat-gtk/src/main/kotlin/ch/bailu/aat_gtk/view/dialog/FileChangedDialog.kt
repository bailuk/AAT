package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.adw.ResponseAppearance
import ch.bailu.gtk.gtk.Window


class FileChangedDialog(window: Window, fileName: String, onResponse: (response: String) -> Unit) {

    init {
        MessageDialog(window, fileName, Res.str().dialog_modified()).apply {
            addResponse(DISCARD, Res.str().dialog_discard())
            setResponseAppearance(DISCARD, ResponseAppearance.DESTRUCTIVE)

            addResponse(CANCEL, Res.str().dialog_cancel())
            setCloseResponse(CANCEL)

            addResponse(SAVE, Res.str().dialog_save())
            setDefaultResponse(SAVE)
            setResponseAppearance(SAVE, ResponseAppearance.SUGGESTED)

            onResponse { response -> onResponse(response.toString()) }

            modal = true
        }.present()
    }

    companion object {
        const val DISCARD = "discard"
        const val SAVE = "save"
        const val CANCEL = "cancel"
    }
}
