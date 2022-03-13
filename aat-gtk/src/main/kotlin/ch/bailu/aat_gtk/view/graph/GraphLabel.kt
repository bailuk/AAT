package ch.bailu.aat_gtk.view.graph

import ch.bailu.aat_lib.view.graph.LabelInterface
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.LabelHelper
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
            labels.put(color, label)
        } else {
            LabelHelper.setLabel(labels[color], text)
        }
    }

    override fun setText(color: Int, text: String, unit: String) {
        setText(color, "$text [$unit]")
    }
}
