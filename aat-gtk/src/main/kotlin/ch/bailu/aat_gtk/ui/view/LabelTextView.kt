package ch.bailu.aat_gtk.ui.view

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation

open class LabelTextView(labelText: String) {

    val layout = Box(Orientation.VERTICAL, 2)

    private val label = Label()
    private val value = Label()


    init {
        label.text = labelText
        layout.append(label)
        layout.append(value)

    }

    fun setLabel(text: String) {
        label.text = text
    }

    fun setValue(text: String) {
        value.text = text
    }
}