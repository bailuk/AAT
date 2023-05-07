package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.view.MainWindow
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.type.Str

fun main() {
    gtkMain()
}

fun gtkMain() {
    App.setup()

    val app = Application(Str(GtkAppConfig.applicationId), ApplicationFlags.FLAGS_NONE)

    app.onActivate {
        try {
            MainWindow(ApplicationWindow(app), app, App.dispatcher)

            App.setupDispatcher()
            App.dispatcher.onResume()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    app.run(0, null)
}
