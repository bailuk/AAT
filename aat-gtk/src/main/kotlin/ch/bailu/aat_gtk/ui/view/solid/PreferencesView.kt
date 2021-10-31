package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Widget
import ch.bailu.gtk.type.Str

abstract class PreferencesView() {
    val container = Box(Orientation.VERTICAL,5)

    init {
        container.borderWidth = 5
    }

    fun add(child: Widget) {
        container.packStart(child, GTK.FALSE, GTK.TRUE, 5)
    }

    fun add(text: String) {
        val label = Label(null)
        label.setMarkup(Str("<b>${text}</b>"))
        add(label)
    }
}