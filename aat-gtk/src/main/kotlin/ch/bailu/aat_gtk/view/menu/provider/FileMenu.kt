package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.controller.FileMenuController
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.preferences.presets.SolidPreset
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.foc.Foc
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gio.MenuModel
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str

class FileMenu(private val appContext: AppContext, private val display: Display): MenuProviderInterface {

    companion object {
        private const val PREFIX = "Detail"

        fun createSelectActivityFolderMenu(prefix: String, directories: List<AppDirectory.GpxDirectoryEntry>): MenuModel {
            return Menu().apply {
                directories.forEachIndexed { index, item ->
                    append(item.name, MenuHelper.toAppAction("$prefix${Strings.ACTION_FILE_COPY_TO}_$index"))
                }
            }
        }
    }

    private var fileMenuController: FileMenuController? = null

    fun setFile(file: Foc) {
        fileMenuController?.file = file
    }

    override fun createMenu(): Menu {
        val directories = AppDirectory.getGpxDirectories(appContext)

        return Menu().apply {
            appendSection(Str.NULL, Menu().apply {
                append(Res.str().clipboard_copy(), MenuHelper.toAppAction(PREFIX + Strings.ACTION_FILE_TO_CLIPBOARD))
                appendSubmenu(Res.str().file_copy(), createSelectActivityFolderMenu(PREFIX, directories))

            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }


    override fun createActions(app: Application) {
        fileMenuController = FileMenuController(PREFIX, app, display, appContext)
    }

    override fun updateActionValues(app: Application) {

    }

}
