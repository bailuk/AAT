package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.menu.provider.MenuProvider
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu

open class PopupButton(private val menuProvider: MenuProvider) {
    private val button = Button()
    private val popover = PopoverMenu.newFromModelFullPopoverMenu(menuProvider.createMenu(), 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popover)

        PopoverMenu(popover.cast()).apply {
            val customWidgets = menuProvider.createCustomWidgets()
            customWidgets.forEach {
                addChild(it.widget, it.id)
            }
            onShow {
                customWidgets.forEach {
                    it.update()
                }
            }
        }

        button.onClicked { popover.popup() }
    }

    fun setIcon(resource: String) {
        button.setIconName(resource)
    }

    fun createActions(app: Application) {
        menuProvider.createActions(app)
    }
}
