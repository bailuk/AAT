package ch.bailu.aat_gtk.util

import ch.bailu.gtk.glib.Glib


object UiThread {

    /**
     * Functions from the gtk namespace do not support calls from outside the main (UI) thread.
     * Glib.idleAdd will add a callback to the main (UI) threads event system.
     */
    fun toUi(function: () -> (Unit)) {
        Glib.idleAdd({ self, _ ->
            function()
            self.unregister()
            false
        }, null)
    }
}
