package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.exit
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.solid.SolidWindowSize
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.dialog.PoiDialog
import ch.bailu.aat_gtk.view.dialog.PreferencesDialog
import ch.bailu.aat_gtk.view.toplevel.list.FileListPage
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.messages.MessageOverlay
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.FileViewSource
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.resources.Res
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

class MainWindow(private val app: Application, private val appContext: AppContext, dispatcher: Dispatcher) : UiController {

    companion object {
        val pageIdCockpit = Icons.incCockpit
        val pageIdFileList = Icons.viewListSymbolic
        val pageIdDetail = Icons.viewContinuousSymbolic
    }

    private val showMapButton = Button().apply {
        setLabel(Res.str().p_map())
        onClicked {
            leaflet.visibleChild = mapView.overlay
        }
    }

    private val customFileSource = FileViewSource(appContext)


    private val headerBar = HeaderBar().apply {
        titleWidget = WindowTitle(GtkAppConfig.appName, GtkAppConfig.appLongName)
    }

    private val stackPage = StackView(headerBar)

    // leaflet containing map and cockpit
    private val leaflet = Leaflet().apply {
        canNavigateBack = false // Disturbs map scrolling
        canNavigateForward = true
        hexpand = true
        vexpand = true
    }

    private val overlay = Overlay()
    private val messageOverlay = MessageOverlay()


    val window = ApplicationWindow(app).apply {
        CSS.addProviderForDisplay(display, Strings.appCss)

        setDefaultSize(
            SolidWindowSize.readWidth(appContext.storage),
            SolidWindowSize.readHeight(appContext.storage)
        )
        setIconName(GtkAppConfig.appId)
        content = overlay

        overlay.addOverlay(messageOverlay.box)
    }

    private val mapView = MapMainView(app, appContext, dispatcher, this, window).apply {
        overlay.setSizeRequest(Layout.mapMinWidth, Layout.windowMinSize)
        onAttached()
    }

    private val detailViewPage = DetailViewPage(this, dispatcher)

    init {
        overlay.child = Box(Orientation.VERTICAL, 0).apply {
            append(leaflet)

        }

        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        dispatcher.addSource(customFileSource)

        stackPage.addView(CockpitPage(appContext,this, dispatcher).box, pageIdCockpit, Res.str().intro_cockpit())
        stackPage.addView(FileListPage(app, appContext, this).vbox, pageIdFileList, Res.str().label_list())
        stackPage.addView(detailViewPage.box, pageIdDetail, Res.str().label_detail())


        leaflet.visibleChild = stackPage.stackPage

        headerBar.packEnd(showMapButton)
        headerBar.packStart(MainMenuButton(app, window, dispatcher, this).menuButton)

        stackPage.restore(appContext.storage)

        window.onCloseRequest {
            SolidWindowSize.writeSize(appContext.storage, window.width, window.height)
            stackPage.save(appContext.storage)
            mapView.onDetached()
            false
        }

        window.onDestroy {
            exit(dispatcher, 0)
        }

    }

    override fun showMap() {
        leaflet.visibleChild = mapView.overlay
    }

    override fun showPoi() {
        PoiDialog.show(this, app)
    }

    override fun frameInMap(info: GpxInformation) {
        if (info.getBoundingBox().hasBounding()) {
            mapView.map.frameBounding(info.getBoundingBox())
        }
    }

    override fun centerInMap(info: GpxInformation) {
        if (info.getBoundingBox().hasBounding()) {
            mapView.map.setCenter(info.getBoundingBox().center.toLatLong())
        }
    }

    override fun load(info: GpxInformation) {
        // TODO saveFile()
        mapView.editDraft()
        customFileSource.setFile(info.file)
        customFileSource.setEnabled(true)
    }

    override fun showCockpit() {
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
        PreferencesDialog.showMap(app, appContext)
    }

    override fun getMapBounding(): BoundingBoxE6 {
        return BoundingBoxE6(mapView.map.boundingBox)
    }

    override fun showFileList() {
        hideMap()
        stackPage.showPage(pageIdFileList)
    }

    override fun showPreferences() {
        PreferencesDialog.show(app, appContext)
    }

    override fun showInDetail(infoID: Int) {
        detailViewPage.select(infoID)
    }

    override fun loadIntoEditor(info: GpxInformation) {
        // TODO saveFile()
        mapView.edit(info)
        customFileSource.setEnabled(false)
    }

    override fun hideMap() {
        leaflet.visibleChild = stackPage.stackPage
    }
}
