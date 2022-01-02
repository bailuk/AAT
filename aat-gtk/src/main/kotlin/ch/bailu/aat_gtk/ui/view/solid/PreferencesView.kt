package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str

abstract class PreferencesView() {
    val scrolled = ScrolledWindow()
    private val container = Box(Orientation.VERTICAL,5)

    init {
        scrolled.child = container
    }

    fun add(child: Widget) {
        container.append(child)
    }

    fun add(text: String) {
        val label = Label(Str.NULL)
        label.setMarkup(Str("<b>${text}</b>"))
        add(label)
    }
}