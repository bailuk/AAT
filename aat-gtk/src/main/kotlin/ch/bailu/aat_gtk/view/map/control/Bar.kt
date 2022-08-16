package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.extensions.setIcon
import ch.bailu.aat_lib.map.edge.EdgeViewInterface
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*

open class Bar(private val pos: Position): EdgeViewInterface {
    companion object {

        const val ICON_SIZE = 24
        const val MARGIN = 6
        const val SIZE = ICON_SIZE + MARGIN * 4

        private fun createBox(pos: Position): Box {
            when (pos) {
                Position.TOP ->
                    return Box(Orientation.HORIZONTAL, 0).apply {
                        halign = Align.FILL
                        valign = Align.START
                    }
                Position.BOTTOM ->
                    return Box(Orientation.HORIZONTAL, 0).apply {
                        halign = Align.FILL
                        valign = Align.END
                    }
                Position.LEFT ->
                    return Box(Orientation.VERTICAL, 0).apply {
                        valign = Align.FILL
                        halign = Align.START
                    }
                Position.RIGHT ->
                    return Box(Orientation.VERTICAL, 0).apply {
                        valign = Align.FILL
                        halign = Align.END
                    }
            }
        }
    }


    val box = createBox(pos).apply { visible = GTK.FALSE }


    fun add(image: String): Button {
        val button = Button()
        add(button)
        button.setIcon(image)
        return button
    }

    fun add(widget: Widget) {
        widget.margin(MARGIN)
        box.append(widget)
    }

    override fun hide() {
        box.visible = GTK.FALSE
    }

    override fun pos(): Position {
        return pos
    }

    override fun show() {
        box.visible = GTK.TRUE
    }
}
