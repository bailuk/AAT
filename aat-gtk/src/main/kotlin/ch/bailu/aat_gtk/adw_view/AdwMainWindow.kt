package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.solid.SolidWindowSize
import ch.bailu.aat_gtk.view.TrackerButtonStartPauseResume
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.toplevel.CockpitView
import ch.bailu.aat_gtk.view.toplevel.DetailView
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_gtk.view.toplevel.list.FileList
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.CustomFileSource
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.gpx.GpxInformation
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
import ch.bailu.gtk.type.Str

class AdwMainWindow(app: Application, dispatcher: Dispatcher) : UiController {

    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services)
    private val mapVisibleButton = ToggleButton()

    private val customFileSource = CustomFileSource(GtkAppContext)

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
        setDefaultSize(SolidWindowSize.readWidth(GtkAppContext.storage), SolidWindowSize.readHeight(GtkAppContext.storage))
        setIconName(GtkAppConfig.applicationId)
        content = toast

        onCloseRequest {
            SolidWindowSize.writeSize(GtkAppContext.storage, width, height)
            false
        }

        onDestroy {
            App.exit(0)
        }
    }

    private val mapView = MapMainView(app, dispatcher, this , GtkAppContext, window).apply {
        overlay.setSizeRequest(Layout.mapMinWidth,Layout.windowMinSize)
        onAttached()
    }

    init {
        toast.child = Box(Orientation.VERTICAL,0).apply {
            append(headerBar)
            append(leaflet)

        }



        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        dispatcher.addSource(customFileSource)
        dispatcher.addTarget(trackerButton, InfoID.ALL)

        stackPage.addView(CockpitView().apply {addDefaults((dispatcher))}.scrolledWindow, "view-grid-symbolic","Cockpit")
        stackPage.addView(FileList(app, GtkAppContext.storage, GtkAppContext, this).vbox,"view-list-symbolic","Tracks")
        stackPage.addView(DetailView(Dispatcher(), GtkAppContext.storage).scrolled,"view-continuous-symbolic", "Detail")

        leaflet.visibleChild = stackPage.stackPage

        mapVisibleButton.iconName = Str("zoom-fit-best-symbolic")
        mapVisibleButton.onToggled {
            if (mapVisibleButton.active) {
                leaflet.visibleChild = mapView.overlay
            } else {
                leaflet.visibleChild = stackPage.stackPage
            }
        }

        headerBar.packStart(trackerButton.button)
        headerBar.packEnd(mapVisibleButton)
        headerBar.packEnd(MainMenuButton(app, window, dispatcher, this).menuButton)
    }

    override fun showMap() {
        mapVisibleButton.active = true
    }

    override fun showPoi() {
        TODO("Not yet implemented")
    }

    override fun frameInMap(info: GpxInformation) {
        if (info.boundingBox.hasBounding()) {
            mapView.map.frameBounding(info.boundingBox)
        }
    }

    override fun centerInMap(info: GpxInformation) {
        if (info.boundingBox.hasBounding()) {
            mapView.map.setCenter(info.boundingBox.center.toLatLong())
        }
    }

    override fun load(info: GpxInformation) {
        customFileSource.setFileID(info.file.toString())
    }

    override fun showCockpit() {
        TODO("Not yet implemented")
    }

    override fun showDetail() {
        TODO("Not yet implemented")
    }

    override fun showInList() {
        TODO("Not yet implemented")
    }

    override fun showPreferencesMap() {
        TODO("Not yet implemented")
    }

    override fun back() {
        TODO("Not yet implemented")
    }

    override fun showContextBar() {
        TODO("Not yet implemented")
    }

    override fun getMapBounding(): BoundingBoxE6 {
        TODO("Not yet implemented")
    }

    override fun showFiles() {
        TODO("Not yet implemented")
    }

    override fun showPreferences() {
        TODO("Not yet implemented")
    }
}
