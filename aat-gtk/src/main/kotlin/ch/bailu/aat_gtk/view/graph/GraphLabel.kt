package ch.bailu.aat_gtk.view.graph

import ch.bailu.aat_gtk.lib.extensions.setText
import ch.bailu.aat_lib.view.graph.LabelInterface
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.type.Str

class GraphLabel : LabelInterface {

    val layout = Box(Orientation.VERTICAL, 0)
    private val labels = HashMap<Int, Label>()

    init {
        layout.marginTop = 5
        layout.marginEnd = 5
        layout.halign = Align.END
        layout.valign = Align.START
    }

    override fun setText(color: Int, text: String) {
        if (!labels.containsKey(color)) {
            println(text)
            val label = Label(Str(text))
            label.xalign = 1f
            layout.append(label)
            labels[color] = label
        } else {
            labels[color]?.apply {
                this.setText(text)
            }
        }
    }

    override fun setText(color: Int, text: String, unit: String) {
        setText(color, "$text [$unit]")
    }
}
