package ch.bailu.aat_gtk.view.toplevel.list.menu

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.provider.CustomWidget
import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.aat_gtk.view.menu.provider.SolidOverlaySelectorMenu
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.IteratorSimple
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

class FileContextMenu(
    private val solidOverlaySelectorMenu: SolidOverlaySelectorMenu,
    private val iteratorSimple: IteratorSimple,
    private val uiController: UiController,
    private val application: Application
) : MenuProvider {

    var index = -1

    fun addToButton(menuButton: MenuButton) {
        menuButton.menuModel = createMenu().create(application)
        PopoverMenu(menuButton.popover.cast()).apply {
            onShow {
                createCustomWidgets().forEach {
                    addChild(it.widget, Str(it.id))
                }
            }
        }
    }

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .submenu(Res.str().file_overlay(), solidOverlaySelectorMenu.createMenu())
            .label(ToDo.translate("Load...")) {
                iteratorSimple.moveToPosition(index)
                uiController.load(iteratorSimple.info)
            }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        iteratorSimple.moveToPosition(index)
        return solidOverlaySelectorMenu.createCustomWidgets(iteratorSimple.info.file)
    }
}
