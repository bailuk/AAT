package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Leaflet
import ch.bailu.gtk.adw.StatusPage
import ch.bailu.gtk.adw.ToastOverlay
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Image
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Stack
import ch.bailu.gtk.type.Str

class AdwMainWindow(app: Application, dispatcher: Dispatcher) {

    val stack = Stack().apply {
        addTitled(StatusPage().apply {
            child = Box(Orientation.VERTICAL, 1).apply {
                title = Str("Call")
                append(Image().apply {
                    setFromIconName("call-start")
                    pixelSize = 128
                    addCssClass("dim-label")
                })
            }
        }, "stack_page1", "Call")

        addTitled(StatusPage().apply {
            title = Str("Sound Card")
            child = Box(Orientation.VERTICAL, 1).apply {
                append(Image().apply {
                    setFromIconName("audio-card")
                    pixelSize = 128
                    addCssClass("dim-label")
                })
            }
        }, "stack_page2", "Sound Card")
    }

    val leaflet = Leaflet().apply {
        canNavigateBack = true
        canNavigateForward = true
        hexpand = true
        vexpand = true

        append(stack) // Cockpit / List / Preferences / ...
    }

    val toast = ToastOverlay()

    val window = ApplicationWindow(app).apply {
        setDefaultSize(800, 576)
        setIconName(GtkAppConfig.applicationId)
        content = Box(Orientation.VERTICAL, 0).apply {
            append(HeaderBar().apply {
                titleWidget = WindowTitle(GtkAppConfig.shortName, GtkAppConfig.longName)
            })
            append(toast)
        }
    }

    init {
        toast.child = leaflet

        val mapView = MapMainView(app, dispatcher, AdwUiController() , GtkAppContext, window)
        mapView.onAttached()

        leaflet.append(mapView.overlay)
    }
}
