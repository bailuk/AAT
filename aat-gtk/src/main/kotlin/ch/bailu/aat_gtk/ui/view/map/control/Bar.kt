package ch.bailu.aat_gtk.ui.view.map.control

import ch.bailu.aat_gtk.ui.util.IconMap
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.MenuButton

class Bar(orientation: Int) {
    companion object {
        const val ICON_SIZE = 24
        const val MARGIN = 6
        const val SIZE= ICON_SIZE + MARGIN * 2
    }

    val box = Box(orientation, 0)


    fun add(image : String) : Button {
        val button = add(Button())
        button.child = IconMap.getImage(image, ICON_SIZE)
        return button
    }

    fun add(button : Button) : Button {
        button.marginBottom = MARGIN
        button.marginEnd    = MARGIN
        button.marginStart  = MARGIN
        button.marginTop    = MARGIN

        box.append(button)
        return button
    }

    fun add(button : MenuButton) : MenuButton {
        button.marginBottom = MARGIN
        button.marginEnd    = MARGIN
        button.marginStart  = MARGIN
        button.marginTop    = MARGIN

        box.append(button)
        return button
    }

}