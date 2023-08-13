package ch.bailu.aat_gtk.view.messages

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.type.Str

class MessageBar(messageType: String, cssClass: String) {
    private val timer = GtkTimer()
    val label = Label(Str.NULL).apply {
        hide()
        addCssClass(cssClass)
        label
    }

    init {
        GtkAppContext.broadcaster.register({
            if (it.size > 1) {
                label.setText(it[1])
                label.show()
                timer.cancel()
                timer.kick(5000) { label.hide() }
            }
        }, messageType)
    }
}
