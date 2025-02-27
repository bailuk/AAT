package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupMenuButtonOverlay(private val menuProvider: MenuProvider) {
    private val button = Button()
    private val popover = PopoverMenu.newFromModelFullPopoverMenu(menuProvider.createMenu(), 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popover)

        PopupMenuButton.setPopover(popover, menuProvider)
        button.onClicked { popover.popup() }
    }

    fun setIcon(iconName: String) {
        button.setIconName(iconName)
    }

    fun setIcon(iconName: Str) {
        button.iconName = iconName
    }

    fun createActions(app: Application) {
        menuProvider.createActions(app)
    }
}
