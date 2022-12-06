package ch.bailu.aat_gtk.view.toplevel.list.menu

import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_gtk.view.menu.provider.CustomWidget
import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.aat_gtk.view.menu.provider.SolidOverlaySelectorMenu
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application

class FileContextMenu(
    private val solidOverlaySelectorMenu: SolidOverlaySelectorMenu,
    private val iteratorSimple: IteratorSimple,
    private val uiController: UiController
) : MenuProvider {

    var index = -1

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSubmenu(Res.str().file_overlay(), solidOverlaySelectorMenu.createMenu())
            append(ToDo.translate("Load..."), "app.fileContextLoad")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        iteratorSimple.moveToPosition(index)
        return solidOverlaySelectorMenu.createCustomWidgets()
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "fileContextLoad") {
            iteratorSimple.moveToPosition(index)
            uiController.load(iteratorSimple.info)
        }
        solidOverlaySelectorMenu.createActions(app)
    }
}
