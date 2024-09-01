package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.adw.MessageDialog
import ch.bailu.gtk.adw.ResponseAppearance

class FileDeleteDialog(app: Application, file: Foc, onResponse: (response: String) -> Unit) {
    init {
        MessageDialog(app.activeWindow, Res.str().file_delete_ask(), file.pathName).apply {
            addResponse(Strings.ID_CANCEL, Res.str().cancel())
            setCloseResponse(Strings.ID_CANCEL)
            addResponse(Strings.ID_OK, Res.str().ok())
            setResponseAppearance(Strings.ID_OK, ResponseAppearance.DESTRUCTIVE)

            onResponse { response -> onResponse(response.toString()) }
            present()
        }
    }
}
