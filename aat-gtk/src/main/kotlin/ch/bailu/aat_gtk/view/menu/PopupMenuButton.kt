package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.Popover
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupMenuButton(private val menuProvider: MenuProvider) {
    val menuButton = MenuButton().apply {
        menuModel = menuProvider.createMenu()

        Companion.setPopover(popover, menuProvider)
    }

    fun createActions(app: Application) {
        menuProvider.createActions(app)
    }

    fun setIcon(iconName: String) {
        menuButton.setIconName(iconName)
    }

    fun setIcon(iconName: Str) {
        menuButton.iconName = iconName
    }

    companion object {
        fun setPopover(popover: Popover, menuProvider: MenuProvider) {
            PopoverMenu(popover.cast()).apply {
                val customWidgets = menuProvider.createCustomWidgets()
                customWidgets.forEach {
                    addChild(it.widget, Str(it.id))
                }
                onShow { customWidgets.forEach { it.update() } }
            }
        }
    }
}
