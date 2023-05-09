package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.TrackerButtonStartPauseResume
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.toplevel.CockpitView
import ch.bailu.aat_gtk.view.toplevel.DetailView
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_gtk.view.toplevel.list.FileList
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Leaflet
import ch.bailu.gtk.adw.ToastOverlay
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.ToggleButton

class AdwMainWindow(app: Application, dispatcher: Dispatcher) {

    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services)
    private val mapVisibleButton = ToggleButton()

    private val stackPage = AdwStackPage()

    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.shortName, GtkAppConfig.longName)
    }

    private val leaflet = Leaflet().apply {
        canNavigateBack = true
        canNavigateForward = true
        hexpand = true
        vexpand = true
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

        val mapView = MapMainView(app, dispatcher, AdwUiController() , GtkAppContext, window).apply {
            overlay.setSizeRequest(300,100)
            onAttached()
        }

        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        dispatcher.addTarget(trackerButton, InfoID.ALL)

        stackPage.addView(CockpitView().apply {addDefaults((dispatcher))}.scrolledWindow, "preferences-desktop","Cockpit")
        stackPage.addView(FileList(app, GtkAppContext.storage, GtkAppContext, AdwUiController()).vbox,"applications-internet","Tracks")
        stackPage.addView(DetailView(Dispatcher(), GtkAppContext.storage).scrolled,"applications-science", "Detail")

        leaflet.visibleChild = stackPage.stackPage

        mapVisibleButton.onToggled {
            if (mapVisibleButton.active) {
                leaflet.visibleChild = mapView.overlay
            } else {
                leaflet.visibleChild = stackPage.stackPage
            }
        }

        headerBar.packStart(trackerButton.button)
        headerBar.packEnd(mapVisibleButton)
        headerBar.packEnd(MainMenuButton(app, window, dispatcher, AdwUiController()).menuButton)
    }
}
