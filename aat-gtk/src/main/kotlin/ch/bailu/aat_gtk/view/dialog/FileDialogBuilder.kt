package ch.bailu.aat_gtk.view.dialog

import ch.bailu.aat_gtk.util.extensions.toPathString
import ch.bailu.foc.Foc
import ch.bailu.foc.FocFile
import ch.bailu.gtk.gio.File
import ch.bailu.gtk.gtk.FileDialog
import ch.bailu.gtk.gtk.Window
import ch.bailu.gtk.type.Str

class FileDialogBuilder {
    private var title = ""
    private var initialFolder = ""

    fun title(title: String): FileDialogBuilder {
        this.title = title
        return this
    }

    fun path(path: String): FileDialogBuilder {
        initialFolder = path
        return this
    }

    fun path(path: Foc): FileDialogBuilder {
        path(path.toPathString())
        return this
    }

    fun open(window: Window, response: (Foc)->Unit) {
        val dialog = createDialog()
        dialog.open(window, null, { self, _, res, _ ->
            returnFile(dialog.openFinish(res), response)
            self.unregister()
        }, null)
    }

    fun selectFolder(window: Window, response: (Foc)->Unit) {
        val dialog = createDialog()
        dialog.selectFolder(window, null, { self, _, res, _ ->
            returnFile(dialog.selectFolderFinish(res), response)
            self.unregister()
        }, null)
    }

    private fun createDialog(): FileDialog {
        val dialog = FileDialog()
        if (title.isNotEmpty()) {
            dialog.setTitle(title)
        }

        if (initialFolder.isNotEmpty()) {
            dialog.initialFolder = File.newForPath(Str(initialFolder))
        }
        return dialog
    }

    private fun returnFile(file: File, response: (Foc)->Unit) {
        if (file.isNotNull) {
            val path = file.path
            if (path.isNotNull) {
                response(FocFile(file.path.toString()))
                path.destroy()
            }
            file.unref()
        }
    }
}
