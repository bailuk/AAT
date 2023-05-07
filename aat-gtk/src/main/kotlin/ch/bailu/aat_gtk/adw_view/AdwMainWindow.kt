package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.TrackerButtonStartPauseResume
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Leaflet
import ch.bailu.gtk.adw.StatusPage
import ch.bailu.gtk.adw.ToastOverlay
import ch.bailu.gtk.adw.ViewStack
import ch.bailu.gtk.adw.ViewSwitcherBar
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Image
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.ToggleButton
import ch.bailu.gtk.type.Str

class AdwMainWindow(app: Application, dispatcher: Dispatcher) {

    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services)
    private val mapVisibleButton = ToggleButton()


    private val viewStack = ViewStack().apply {
        vexpand = true
    }

    val viewStackSwitcher = ViewSwitcherBar().apply {
        stack = viewStack
        reveal = true
    }

    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.shortName, GtkAppConfig.longName)
        packStart(trackerButton.button)
    }

    private val stackPage = Box(Orientation.VERTICAL,0).apply {
        append(viewStack)
        append(viewStackSwitcher)

    }

    private val leaflet = Leaflet().apply {
        canNavigateBack = true
        canNavigateForward = true
        hexpand = true
        vexpand = true

        append(stackPage) // Cockpit / List / Preferences / ...
    }

    private val toast = ToastOverlay()


    val window = ApplicationWindow(app).apply {
        setDefaultSize(800, 576)
        setIconName(GtkAppConfig.applicationId)
        content = toast

        onDestroy {
            App.exit(0)
        }
    }

    init {
        toast.child = Box(Orientation.VERTICAL,0).apply {
            append(headerBar)
            append(leaflet)

        }

        val mapView = MapMainView(app, dispatcher, AdwUiController() , GtkAppContext, window)
        mapView.onAttached()
        mapView.overlay.setSizeRequest(300,100)

        leaflet.append(mapView.overlay)

        dispatcher.addTarget(trackerButton, InfoID.ALL)

        addTestStatus("Cockpit", "preferences-desktop")
        addTestStatus("Detail", "applications-science")
        addTestStatus("Tracks", "applications-internet")

        leaflet.visibleChild = stackPage

        mapVisibleButton.onToggled {
            if (mapVisibleButton.active) {
                leaflet.visibleChild = mapView.overlay
            } else {
                leaflet.visibleChild = stackPage
            }
        }

        headerBar.packEnd(mapVisibleButton)
        headerBar.packEnd(MainMenuButton(app, window, dispatcher, AdwUiController()).menuButton)
    }

    private fun addTestStatus(titleName: String, iconName: String) {
        viewStack.addTitledWithIcon(StatusPage().apply {
            title = Str(titleName)
            child = Box(Orientation.VERTICAL, 1).apply {
                append(Image().apply {
                    setFromIconName(iconName)
                    pixelSize = 128
                })
            }
        }, iconName, titleName, iconName)
    }
}
