package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.MenuItem
import ch.bailu.gtk.glib.Variant
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.lib.handler.action.ActionHandler
import ch.bailu.gtk.type.Str

object MenuHelper {
    fun setAction(app: Application, action: String, onActivate: ()->Unit) {
        ActionHandler.get(app, action).apply {
            disconnectSignals()
            onActivate(onActivate)
        }
    }

    fun createCustomItem(customId: String): MenuItem {
        return MenuItem(Res.str().tracker(), "app.$customId").apply {
            setAttribute("custom", null)
            setAttributeValue("custom", Variant.newStringVariant(customId))
        }
    }

    fun addToButton(menuButton: MenuButton, menuProvider: MenuProvider) {
        menuButton.menuModel = menuProvider.createMenu()
        PopoverMenu(menuButton.popover.cast()).apply {
            menuProvider.createCustomWidgets().forEach {
                addChild(it.widget, Str(it.id))
            }
        }
    }
}
