package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.menu.model.Menu
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.helper.ActionHelper

open class PopupButton(actionHelper: ActionHelper, model: Menu) {
    private val button = Button()
    private val popup = PopoverMenu.newFromModelFullPopoverMenu(GtkMenu(actionHelper, model).menu, 0)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popup)
        button.onClicked { popup.popup() }
    }

    fun setIcon(iconResource: String) {
        Bar.setIcon(button, iconResource)
    }
}
