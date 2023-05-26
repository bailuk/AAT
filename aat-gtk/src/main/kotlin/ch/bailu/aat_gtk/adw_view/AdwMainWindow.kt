package ch.bailu.aat_gtk.adw_view

import ch.bailu.aat_gtk.app.App
import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.solid.SolidWindowSize
import ch.bailu.aat_gtk.view.TrackerButtonStartPauseResume
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.messages.MessageOverlay
import ch.bailu.aat_gtk.view.toplevel.CockpitPage
import ch.bailu.aat_gtk.view.toplevel.DetailViewPage
import ch.bailu.aat_gtk.view.toplevel.MapMainView
import ch.bailu.aat_gtk.view.toplevel.list.FileList
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.FileViewSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.adw.HeaderBar
import ch.bailu.gtk.adw.Leaflet
import ch.bailu.gtk.adw.WindowTitle
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.lib.bridge.CSS

class AdwMainWindow(private val app: Application, dispatcher: Dispatcher) : UiController {

    companion object {
        const val pageIdCockpit = "view-grid-symbolic"
        const val pageIdFileList = "view-list-symbolic"
        const val pageIdDetail = "view-continuous-symbolic"
    }

    private val showMapButton = Button()

    private val customFileSource =
        FileViewSource(GtkAppContext)


    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.shortName, GtkAppConfig.longName)
    }

    private val stackPage = AdwStackView(headerBar)

    private val leaflet = Leaflet().apply {
        canNavigateBack = true
        canNavigateForward = true
        hexpand = true
        vexpand = true
    }

    private val overlay = Overlay()
    private val messageOverlay = MessageOverlay()


    val window = ApplicationWindow(app).apply {
        setDefaultSize(SolidWindowSize.readWidth(GtkAppContext.storage), SolidWindowSize.readHeight(GtkAppContext.storage))
        setIconName(GtkAppConfig.applicationId)
        content = overlay

        overlay.addOverlay(messageOverlay.box)

        CSS.addProviderForDisplay(display, Strings.appCss)
    }

    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services, window, dispatcher, this)

    private val mapView = MapMainView(app, dispatcher, this , GtkAppContext, window).apply {
        overlay.setSizeRequest(Layout.mapMinWidth,Layout.windowMinSize)
        onAttached()
    }

    private val detailViewPage = DetailViewPage(this, dispatcher)

    init {
        overlay.child = Box(Orientation.VERTICAL,0).apply {
            append(leaflet)

        }

        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        dispatcher.addSource(customFileSource)
        dispatcher.addTarget(trackerButton, InfoID.ALL)

        stackPage.addView(CockpitPage(this, dispatcher, window).box, pageIdCockpit,"Cockpit")
        stackPage.addView(FileList(app, GtkAppContext.storage, GtkAppContext, this).vbox, pageIdFileList,"Tracks")
        stackPage.addView(detailViewPage.box, pageIdDetail, "Detail")


        leaflet.visibleChild = stackPage.stackPage

        showMapButton.iconName = Strings.iconFrame
        showMapButton.onClicked {
           leaflet.visibleChild = mapView.overlay
        }

        headerBar.packEnd(showMapButton)
        headerBar.packStart(MainMenuButton(app, window, dispatcher, this).menuButton)

        stackPage.restore(GtkAppContext.storage)

        window.onCloseRequest {
            SolidWindowSize.writeSize(GtkAppContext.storage, window.width, window.height)
            stackPage.save(GtkAppContext.storage)
            mapView.onDetached()
            false
        }

        window.onDestroy {
            App.exit(0)
        }

    }

    override fun showMap() {
        leaflet.visibleChild = mapView.overlay
    }

    override fun showPoi() {
        AdwPoiDialog.show(this, app)
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
        // TODO saveFile()
        mapView.editDraft()
        customFileSource.setFileID(info.file.toString())
        customFileSource.enable()
    }

    override fun showCockpit() {
        AppLog.e("Error cockpit")
        leaflet.visibleChild = stackPage.stackPage
        stackPage.showPage(pageIdCockpit)
    }

    override fun showDetail() {
        leaflet.visibleChild = stackPage.stackPage
        stackPage.showPage(pageIdDetail)
    }

    override fun showInList() {
        TODO("Not yet implemented")
    }

    override fun showPreferencesMap() {
        AdwPreferencesDialog.showMap(app)
    }

    override fun getMapBounding(): BoundingBoxE6 {
        return BoundingBoxE6(mapView.map.boundingBox)
    }

    override fun showFileList() {
        hideMap()
        stackPage.showPage(pageIdFileList)
    }

    override fun showPreferences() {
        AdwPreferencesDialog.show(app)
    }

    override fun showInDetail(infoID: Int) {
        detailViewPage.select(infoID)
    }

    override fun loadIntoEditor(info: GpxInformation) {
        // TODO saveFile()
        mapView.edit(info)
        customFileSource.disable()
    }

    override fun hideMap() {
        leaflet.visibleChild = stackPage.stackPage
    }
}
