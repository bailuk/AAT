package ch.bailu.aat_gtk.view.map.control

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_lib.map.edge.EdgeViewInterface
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

open class Bar(private val pos: Position): EdgeViewInterface {
    companion object {
        private fun createBox(pos: Position): Box {
            when (pos) {
                Position.TOP ->
                    return Box(Orientation.HORIZONTAL, 0).apply {
                        halign = Align.CENTER
                        valign = Align.START
                    }
                Position.BOTTOM ->
                    return Box(Orientation.HORIZONTAL, 0).apply {
                        halign = Align.CENTER
                        valign = Align.END
                    }
                Position.LEFT ->
                    return Box(Orientation.VERTICAL, 0).apply {
                        valign = Align.CENTER
                        halign = Align.START
                    }
                Position.RIGHT ->
                    return Box(Orientation.VERTICAL, 0).apply {
                        valign = Align.CENTER
                        halign = Align.END
                    }
            }
        }
    }

    val box = createBox(pos).apply {
        visible = false
        addCssClass(Strings.mapControl)
    }

    fun add(iconName: Str): Button {
        val button = Button()
        add(button)
        button.iconName = iconName
        return button
    }

    fun add(widget: Widget) {
        widget.margin(Layout.margin)
        box.append(widget)
    }

    override fun hide() {
        box.visible = false
    }

    override fun pos(): Position {
        return pos
    }

    override fun show() {
        box.visible = true
    }
}
