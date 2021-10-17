package ch.bailu.aat_gtk.app

import ch.bailu.gtk.GTK
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.ApplicationWindow
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.type.Str
import ch.bailu.gtk.type.Strs

fun main(args: Array<String>) {
    GTK.init();

    val app = Application(
        Str("ch.bailu.aat_gtk"),
        ApplicationFlags.FLAGS_NONE
    )

    app.onActivate {

        // Create a new window
        val window = ApplicationWindow(app)

        // Create a new button
        val button =
            Button.newWithLabelButton(Str("Hello, World!"))

        // When the button is clicked, close the window
        button.onClicked { window.close() }
        window.add(button)
        window.showAll()
    }

    app.run(args.size, Strs(args))
}