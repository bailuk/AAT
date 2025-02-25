package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.adw.ResponseAppearance
import ch.bailu.gtk.gtk.Window


class FileChangedDialog(window: Window, fileName: String, onResponse: (response: String) -> Unit) {

    init {
        MessageDialog(window, fileName, Res.str().dialog_modified()).apply {
            addResponse(Strings.ID_DISCARD, Res.str().dialog_discard())
            setResponseAppearance(Strings.ID_DISCARD, ResponseAppearance.DESTRUCTIVE)

            addResponse(Strings.ID_CANCEL, Res.str().dialog_cancel())
            setCloseResponse(Strings.ID_CANCEL)

            addResponse(Strings.ID_SAVE, Res.str().dialog_save())
            setDefaultResponse(Strings.ID_SAVE)
            setResponseAppearance(Strings.ID_SAVE, ResponseAppearance.SUGGESTED)

            onResponse { response -> onResponse(response.toString()) }

            modal = true
        }.present()
    }
}
