package ch.bailu.aat_gtk.view.messages

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.type.Str

class MessageBar(messageType: String, cssClass: String) {
    private val timer = GtkTimer()
    val label = Label(Str.NULL).apply {
        visible = false
        addCssClass(cssClass)
        label
    }

    init {
        GtkAppContext.broadcaster.register(messageType) {
            if (it.size > 1) {
                label.setText(it[1])
                label.visible = true
                timer.cancel()
                timer.kick(5000) { label.visible = false }
            }
        }
    }
}
