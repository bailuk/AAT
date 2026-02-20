package ch.bailu.aat_gtk.view.menu.controller

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gtk.Application

class FileMenuController(
    private val prefix: String,
    private val app: Application,
    private val display: Display,
    private val appContext: AppContext
) {

    fun createFileActions(fileProvider: () -> Foc) {
        createClipboardAction(fileProvider)
        createDirectoryActions { destination ->
            FileAction.copyToDir(appContext, fileProvider(), destination)
        }
    }

    fun createClipboardAction(fileProvider: () -> Foc) {
        MenuHelper.setAction(app, prefix + Strings.ACTION_FILE_TO_CLIPBOARD) {
            ClipboardController(display).setText(fileProvider().pathName)
        }
    }

    fun createDirectoryActions(onFileAction: (file: Foc) -> Unit) {
        val directories = AppDirectory.getGpxDirectories(appContext)
        directories.forEachIndexed { index, item ->
            MenuHelper.setAction(app, "$prefix${Strings.ACTION_FILE_COPY_TO}_$index") {
                onFileAction(item.file)
            }
        }
    }
}
