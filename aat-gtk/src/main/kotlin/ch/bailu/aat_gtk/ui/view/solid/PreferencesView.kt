package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

abstract class PreferencesView() {
    val scrolled = ScrolledWindow(null, null)
    private val container = Box(Orientation.VERTICAL,5)

    init {
        container.borderWidth = 5
        scrolled.add(container)
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