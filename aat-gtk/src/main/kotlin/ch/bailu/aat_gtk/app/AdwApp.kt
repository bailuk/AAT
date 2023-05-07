package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.adw_view.AdwMainWindow
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.gio.ApplicationFlags


fun main() {
    adwMain()
}

fun adwMain() {
    App.setup()

    val app = Application(GtkAppConfig.applicationId, ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        AdwMainWindow(app, App.dispatcher).window.show()
        App.setupDispatcher()
        App.dispatcher.onResume()
    }

    app.run(0, null)
}
