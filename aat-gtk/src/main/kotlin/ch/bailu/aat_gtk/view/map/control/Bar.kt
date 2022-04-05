package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.view.util.margin
import ch.bailu.aat_gtk.view.util.setIcon
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Widget

class Bar(orientation: Int) {
    companion object {
        const val ICON_SIZE = 24
        const val MARGIN = 6
        const val SIZE = ICON_SIZE + MARGIN * 4
    }

    val box = Box(orientation, 0)

    fun add(image : String) : Button {
        val button = Button()
        add(button)
        button.setIcon(image)
        return button
    }

    fun add(widget : Widget) {
        widget.margin(MARGIN)
        box.append(widget)
    }
}
