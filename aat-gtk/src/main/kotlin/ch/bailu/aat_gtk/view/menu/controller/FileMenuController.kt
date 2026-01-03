package ch.bailu.aat_gtk.view.menu.controller

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.ClipboardController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.aat_lib.util.fs.FileAction
import ch.bailu.foc.Foc
import ch.bailu.foc.FocName
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gtk.Application

class FileMenuController(prefix: String, app: Application, display: Display, appContext: AppContext) {

    var file: Foc = FocName.FOC_NULL

    init {
        MenuHelper.setAction(app, prefix + Strings.ACTION_FILE_TO_CLIPBOARD) {
            ClipboardController(display).setText(file.pathName)
        }

        val directories = AppDirectory.getGpxDirectories(appContext)
        directories.forEachIndexed { index, item ->
            MenuHelper.setAction(app, "$prefix${Strings.ACTION_FILE_COPY_TO}_$index") {
                FileAction.copyToDir(appContext, file, item.file)
            }
        }

    }
}
