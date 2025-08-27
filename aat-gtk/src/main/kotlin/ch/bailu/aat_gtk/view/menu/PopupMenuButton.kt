package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProviderInterface
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.MenuButton
import ch.bailu.gtk.gtk.Popover
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupMenuButton(private val menuProvider: MenuProviderInterface): MenuButtonInterface {
    val menuButton = MenuButton().apply {
        menuModel = menuProvider.createMenu()

        Companion.setPopover(popover, menuProvider)
    }

    override fun createActions(app: Application) {
        menuProvider.createActions(app)
    }

    override fun updateActionValues(app: Application) {
        menuProvider.updateActionValues(app)
    }

    override fun setIcon(iconName: String) {
        menuButton.setIconName(iconName)
    }

    override fun setIcon(iconName: Str) {
        menuButton.iconName = iconName
    }

    companion object {
        fun setPopover(popover: Popover, menuProvider: MenuProviderInterface) {
            PopoverMenu(popover.cast()).apply {
                val customWidgets = menuProvider.createCustomWidgets()
                customWidgets.forEach {
                    addChild(it.widget, Str(it.id))
                }
                onShow {
                    customWidgets.forEach { it.update() } }
            }
        }
    }
}
