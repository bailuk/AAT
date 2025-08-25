package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProviderInterface
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupMenuButtonOverlay(private val menuProvider: MenuProviderInterface): MenuButtonInterface {
    private val button = Button()
    private val popover = PopoverMenu.newFromModelFullPopoverMenu(menuProvider.createMenu(), 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popover)

        PopupMenuButton.setPopover(popover, menuProvider)
        button.onClicked { popover.popup() }
    }

    override fun setIcon(iconName: String) {
        button.setIconName(iconName)
    }

    override fun setIcon(iconName: Str) {
        button.iconName = iconName
    }

    override fun createActions(app: Application) {
        menuProvider.createActions(app)
    }
}
