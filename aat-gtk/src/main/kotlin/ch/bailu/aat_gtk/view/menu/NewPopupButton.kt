package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.util.setIcon
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PopoverMenu
import ch.bailu.gtk.type.Str

open class NewPopupButton(menuModel: Menu, customWidgets: Array<CustomWidget> = arrayOf()) {
    private val button = Button()
    private val popup = PopoverMenu.newFromModelPopoverMenu(menuModel)

    val overlay = Overlay()

    init {
        overlay.child = button
        overlay.addOverlay(popup)

        customWidgets.forEach {
            popup.addChild(it.widget, Str(it.id))
        }

        button.onClicked {
            popup.popup()
        }
    }

    fun setIcon(resource: String, size: Int = Bar.ICON_SIZE) {
        button.setIcon(resource, size)
    }
}
