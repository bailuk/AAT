package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.util.IconMap
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Widget

class Bar(orientation: Int) {
    companion object {
        const val ICON_SIZE = 24
        const val MARGIN = 6
        const val SIZE = ICON_SIZE + MARGIN * 4

        fun setMargin(widget: Widget) {
            widget.apply {
                marginBottom = MARGIN
                marginEnd    = MARGIN
                marginStart  = MARGIN
                marginTop    = MARGIN
            }
        }

        fun setIcon(button: Button, imageResource: String) {
            val image = IconMap.getImage(imageResource, ICON_SIZE)
            setMargin(image)
            button.child = image
        }
    }

    val box = Box(orientation, 0)

    fun add(image : String) : Button {
        val button = Button()
        add(button)
        setIcon(button, image)
        return button
    }

    fun add(widget : Widget) {
        setMargin(widget)
        box.append(widget)
    }
}
