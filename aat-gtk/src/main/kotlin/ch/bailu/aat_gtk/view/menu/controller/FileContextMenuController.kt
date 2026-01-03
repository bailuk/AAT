package ch.bailu.aat_gtk.view.menu.controller

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.dialog.FileDeleteDialog
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.location.SolidMockLocationFile
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.FileAccess
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.adw.AlertDialog
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Entry
import ch.bailu.gtk.gtk.Window

class FileContextMenuController(app: Application, appContext: AppContext) {

    private val solidMock = SolidMockLocationFile(appContext.storage)
    var file: Foc = FocName.FOC_NULL

    init {
        MenuHelper.setAction(app, Strings.ACTION_FILE_MOCK) {
            solidMock.setValue(file.path)
        }

        MenuHelper.setAction(app, Strings.ACTION_FILE_DELETE) {
            delete(app.activeWindow)
        }

        MenuHelper.setAction(app, Strings.ACTION_FILE_RENAME) {
            rename(app.activeWindow)
        }

        MenuHelper.setAction(app, Strings.ACTION_FILE_RELOAD) {
            FileAction.reloadPreview(appContext, file)
        }
    }

    private fun delete(window: Window) {
        if (file.canWrite()) {
            FileDeleteDialog(window, file) { response ->
                if (Strings.ID_OK == response) {
                    file.rm()
                    FileAction.rescanDirectory(GtkAppContext, file)
                }
            }
        } else {
            FileAccess.logErrorReadOnly(file)
        }
    }


    private fun rename(window: Window) {
        if (file.canWrite() && file.hasParent()) {
            val directory = file.parent()
            val dialog = AlertDialog(Res.str().file_rename(), file.name)
            val entry = Entry()
            dialog.extraChild = entry
            dialog.addResponse(Strings.ID_CANCEL, Res.str().cancel())
            dialog.addResponse(Strings.ID_OK, Res.str().ok())
            dialog.onResponse {
                val res = it.toString()
                if (Strings.ID_OK == res) {
                    val source = directory.child(file.name)
                    val target = directory.child(entry.asEditable().text.toString())

                    FileAction.rename(GtkAppContext, source, target)
                }
            }
            dialog.present(window)
        }
    }
}
