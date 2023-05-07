package ch.bailu.aat_gtk.app

import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Leaflet
import ch.bailu.gtk.adw.LeafletPage
import ch.bailu.gtk.adw.StatusPage
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gio.ApplicationFlags
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Image
import ch.bailu.gtk.gtk.Label
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Separator
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.gtk.StackSidebar
import ch.bailu.gtk.type.Str


fun main() {

    val app = Application(GtkAppConfig.applicationId, ApplicationFlags.FLAGS_NONE)




    app.onActivate {
        val stack = Stack()
        val stackSideBar = StackSidebar()
        stackSideBar.stack = stack
        stackSideBar.vexpand = true
        initStack(stack)

        val window = ApplicationWindow(app)
        window.setDefaultSize(800, 576)

        window.content = Leaflet().apply {
            canNavigateBack = true
            vexpand = true

            append(Box(Orientation.VERTICAL, 1).apply {
                vexpand = true
                append(HeaderBar().apply {
                    titleWidget = WindowTitle("Hallo", "Holla")
                })
                append(stackSideBar)
            })

            append(Separator(Orientation.VERTICAL))

            append(Box(Orientation.VERTICAL, 1).apply {
                hexpand = true
                vexpand = true
                append(HeaderBar().apply {
                    titleWidget = WindowTitle("Hallo", "Holla")

                })
                append(stack)
            })
        }
        window.show()
    }

    app.run(0, null)
}


fun initStack(stack: Stack) {
    stack.vexpand = true
    stack.vhomogeneous = false
    stack.addTitled(StatusPage().apply {
        val pageTitle = Str("Call")
        child = Box(Orientation.VERTICAL, 1).apply {
            title = pageTitle
            append(Image().apply {
                setFromIconName("call-start")
                pixelSize = 64
                addCssClass("dim-label")
            })
        }
    }, "stack_page1", "Clall")

    stack.addTitled(StatusPage().apply {
        title = Str("Sound Card")
        child = Box(Orientation.VERTICAL, 1).apply {
            append(Image().apply {
                setFromIconName("audio-card")
                pixelSize = 64
                addCssClass("dim-label")
            })
        }
    }, "stack_page2", "Sound Card")

}
