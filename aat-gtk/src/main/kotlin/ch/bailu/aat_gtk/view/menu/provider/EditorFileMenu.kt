package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.controller.FileMenuController
import ch.bailu.aat_gtk.view.menu.provider.FileMenu.Companion.createSelectActivityFolderMenu
import ch.bailu.aat_lib.api.brouter.BrouterApi
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.preferences.map.overlay.SolidBrouterOverlay
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gdk.Display
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.lib.handler.action.ActionHandler

class EditorFileMenu(private val appContext: AppContext, private val display: Display, private val edit: EditorSourceInterface): MenuProviderInterface {
    companion object {
        private const val PREFIX_BROUTER = "Brouter"
        private const val PREFIX_EDITOR  = "Editor"
    }

    override fun createMenu(): Menu {
        val directories = AppDirectory.getGpxDirectories(appContext)

        return Menu().apply {
            appendSection(ToDo.translate("Editor"), Menu().apply {
                append(Res.str().edit_save(), "app.editSave")
                appendSubmenu(Res.str().edit_save_copy_to(), createSelectActivityFolderMenu(PREFIX_EDITOR, directories))
            })
            appendSection(BrouterApi.NAME, Menu().apply {
                append(ToDo.translate("Load"), "app.brouter")
                append(Res.str().clipboard_copy(), MenuHelper.toAppAction(PREFIX_BROUTER + Strings.ACTION_FILE_TO_CLIPBOARD))
                appendSubmenu(Res.str().file_copy(), createSelectActivityFolderMenu(PREFIX_BROUTER, directories))

            })
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }

    override fun createActions(app: Application) {
        setAction(app, "editSave") { edit.editor.save()}

        val solidBrouterOverlay = SolidBrouterOverlay(appContext.dataDirectory)

        FileMenuController(PREFIX_BROUTER,  app, display, appContext)
            .createFileActions { solidBrouterOverlay.getValueAsFile() }

        FileMenuController(PREFIX_EDITOR, app, display, appContext)
            .createDirectoryActions { destination ->
            edit.editor.saveTo(destination)
        }
    }

    override fun updateActionValues(app: Application) {}

    private fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, action).apply {
            disconnectSignals()
            onActivate(onActivate)
        }
    }
}
