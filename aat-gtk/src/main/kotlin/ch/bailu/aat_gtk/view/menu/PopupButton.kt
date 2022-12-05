package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.setIcon
import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupButton(app: Application, menuProvider: MenuProvider) {
    private val button = Button()
    private val popover = PopoverMenu.newFromModelFullPopoverMenu(menuProvider.createMenu(), 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popover)

        menuProvider.createActions(app) // Todo: is this the right place?

        PopoverMenu(popover.cast()).apply {
            menuProvider.createCustomWidgets().forEach {
                addChild(it.widget, it.id)
            }
        }

        button.onClicked {
            popover.popup()
        }
    }

    fun setIcon(resource: String, size: Int = Layout.iconSize) {
        button.setIcon(resource, size)
    }
}
