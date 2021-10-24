package ch.bailu.aat_gtk.app

import ch.bailu.aat_gtk.app.window.MainWindow
import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs

fun main(args: Array<String>) {
    GTK.init()

    val app = Application(Str("ch.bailu.aat_gtk"), ApplicationFlags.FLAGS_NONE)

    app.onActivate { MainWindow(app) }
    app.run(args.size, Strs(args))
}
