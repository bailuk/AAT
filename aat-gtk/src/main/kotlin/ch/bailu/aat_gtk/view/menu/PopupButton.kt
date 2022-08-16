package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.lib.extensions.setIcon
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class PopupButton(app: Application, menuProvider: MenuProvider) {
    private val button = Button()
    private val popover = PopoverMenu.newFromModelFullPopoverMenu(menuProvider.createMenu().create(app), 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popover)

        PopoverMenu(popover.cast()).apply {
            onShow {
                menuProvider.createCustomWidgets().forEach {
                    addChild(it.widget, Str(it.id))
                }
            }
        }

        button.onClicked {
            popover.popup()
        }
    }

    fun setIcon(resource: String, size: Int = Bar.ICON_SIZE) {
        button.setIcon(resource, size)
    }
}
