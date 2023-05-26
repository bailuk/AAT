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
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.ToggleButton
import ch.bailu.gtk.lib.bridge.CSS

class AdwMainWindow(private val app: Application, dispatcher: Dispatcher) : UiController {

    companion object {
        const val pageIdCockpit = "view-grid-symbolic"
        const val pageIdFileList = "view-list-symbolic"
        const val pageIdDetail = "view-continuous-symbolic"
    }


    private val trackerButton = TrackerButtonStartPauseResume(GtkAppContext.services)
    private val mapVisibleButton = ToggleButton()

    private val customFileSource =
        FileViewSource(GtkAppContext)

    private val stackPage = AdwStackView()

    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.shortName, GtkAppConfig.longName)
    }

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

    private val mapView = MapMainView(app, dispatcher, this , GtkAppContext, window).apply {
        overlay.setSizeRequest(Layout.mapMinWidth,Layout.windowMinSize)
        onAttached()
    }

    private val detailViewPage = DetailViewPage(this, dispatcher)

    init {
        overlay.child = Box(Orientation.VERTICAL,0).apply {
            append(headerBar)
            append(leaflet)

        }

        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        dispatcher.addSource(customFileSource)
        dispatcher.addTarget(trackerButton, InfoID.ALL)

        stackPage.addView(CockpitPage(this, dispatcher).box, pageIdCockpit,"Cockpit")
        stackPage.addView(FileList(app, GtkAppContext.storage, GtkAppContext, this).vbox, pageIdFileList,"Tracks")
        stackPage.addView(detailViewPage.box, pageIdDetail, "Detail")


        leaflet.visibleChild = stackPage.stackPage

        mapVisibleButton.iconName = Strings.iconFrame
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
        mapVisibleButton.active = true
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
        mapVisibleButton.active = false
        stackPage.showPage(pageIdCockpit)
    }

    override fun showDetail() {
        mapVisibleButton.active = false
        stackPage.showPage(pageIdDetail)
    }

    override fun showInList() {
        TODO("Not yet implemented")
    }

    override fun showPreferencesMap() {
        AdwPreferencesDialog.showMap(app)
    }

    override fun back() {
        TODO("Not yet implemented")
    }

    override fun showContextBar() {
        TODO("Not yet implemented")
    }

    override fun getMapBounding(): BoundingBoxE6 {
        return BoundingBoxE6(mapView.map.boundingBox)
    }

    override fun showFileList() {
        mapVisibleButton.active = false
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
}
