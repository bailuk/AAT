package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.gtk.adw.AlertDialog
import ch.bailu.gtk.adw.ResponseAppearance
import ch.bailu.gtk.gtk.Window

class FileDeleteDialog(window: Window, file: Foc, onResponse: (response: String) -> Unit) {
    init {
        AlertDialog(Res.str().file_delete_ask(), file.pathName).apply {
            addResponse(Strings.ID_CANCEL, Res.str().cancel())
            setCloseResponse(Strings.ID_CANCEL)
            addResponse(Strings.ID_OK, Res.str().ok())
            setResponseAppearance(Strings.ID_OK, ResponseAppearance.DESTRUCTIVE)

            onResponse { response -> onResponse(response.toString()) }
            present(window)
        }
    }
}
