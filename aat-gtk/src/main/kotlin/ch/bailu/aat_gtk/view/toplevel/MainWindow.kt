package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.app.exit
import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.solid.SolidWindowSize
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.dialog.FileChangedDialog
import ch.bailu.aat_gtk.view.dialog.PoiDialog
import ch.bailu.aat_gtk.view.dialog.PreferencesDialog
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.MainMenuButton
import ch.bailu.aat_gtk.view.messages.MessageOverlay
import ch.bailu.aat_gtk.view.toplevel.list.FileListPage
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.EditorSource
import ch.bailu.aat_lib.dispatcher.source.FileViewSource
import ch.bailu.aat_lib.dispatcher.source.FixedOverlaySource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
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

class MainWindow(private val app: Application, private val appContext: AppContext, dispatcher: Dispatcher) :
    UiControllerInterface {

    companion object {
        val pageIdCockpit  = Icons.incCockpitSymbolic
        val pageIdFileList = Icons.viewListSymbolic
        val pageIdDetail   = Icons.viewContinuousSymbolic
    }

    private val showMapButton = Button().apply {
        setLabel(Res.str().p_map())
        onClicked {
            leaflet.visibleChild = mapView.overlay
        }
    }

    private val usageTrackers = UsageTrackers()
    private val editorSource = EditorSource(appContext, usageTrackers)
    private val customFileSource = FileViewSource(appContext, usageTrackers)
    private val metaInfoCollector = MetaInfoCollector()

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

    private val mapView = MapMainView(app, appContext, dispatcher, usageTrackers, this, editorSource, window, ).apply {
        overlay.setSizeRequest(Layout.mapMinWidth, Layout.windowMinSize)
        onAttached()
    }

    private val detailViewPage = DetailViewPage(this, dispatcher, usageTrackers.createSelectableUsageTracker())

    init {
        overlay.child = Box(Orientation.VERTICAL, 0).apply {
            append(leaflet)

        }

        leaflet.append(stackPage.stackPage)
        leaflet.append(mapView.overlay)

        setupDispatcher(dispatcher)

        stackPage.addView(CockpitPage(appContext,this, dispatcher).box, pageIdCockpit, Res.str().intro_cockpit())
        stackPage.addView(FileListPage(app, appContext, this).vbox, pageIdFileList, Res.str().label_list())
        stackPage.addView(detailViewPage.box, pageIdDetail, Res.str().label_detail())


        leaflet.visibleChild = stackPage.stackPage

        headerBar.packEnd(showMapButton)
        headerBar.packStart(MainMenuButton(window, dispatcher, this).apply {
            createActions(app)
        }.menuButton)

        stackPage.restore(appContext.storage)

        var blockCloseRequest = true

        window.onCloseRequest {
            if (blockCloseRequest) {
                clearEditor {
                    SolidWindowSize.writeSize(appContext.storage, window.width, window.height)
                    stackPage.save(appContext.storage)
                    mapView.onDetached()
                    blockCloseRequest = false
                    window.close()
                }
            }
            blockCloseRequest
        }

        window.onDestroy {
            exit(dispatcher, 0)
        }

    }

    private fun clearEditor(onCleared: ()->Unit)  {
        if (editorSource.editor.isModified()) {
            FileChangedDialog(window, editorSource.file.name)  { response ->
                if (response == Strings.ID_DISCARD) {
                    onCleared()
                } else if (response == Strings.ID_SAVE) {
                    editorSource.editor.save()
                    onCleared()
                }
            }
        } else {
            onCleared()
        }
    }

    override fun showMap() {
        leaflet.visibleChild = mapView.overlay
    }

    override fun showPoi() {
        PoiDialog.show(this, app)
    }

    override fun frameInMap(info: GpxInformation) {
        frameInMap(info.getBoundingBox())
    }

    override fun frameInMap(iid: Int) {
        frameInMap(metaInfoCollector.getBounding(iid))
    }

    private fun frameInMap(boundingBoxE6: BoundingBoxE6) {
        if (boundingBoxE6.hasBounding()) {
            mapView.map.frameBounding(boundingBoxE6)
        }
    }
    override fun centerInMap(info: GpxInformation) {
        if (info.getBoundingBox().hasBounding()) {
            mapView.map.setCenter(info.getBoundingBox().center.toLatLong())
        }
    }

    override fun centerInMap(infoID: Int) {
        if (infoID == InfoID.TRACKER) {
             SolidPositionLock(appContext.storage, GtkCustomMapView.DEFAULT_KEY).value = true
        } else if (metaInfoCollector.hasBounding(infoID)) {
            mapView.map.setCenter(metaInfoCollector.getBounding(infoID).center.toLatLong())
        }
    }

    override fun load(info: GpxInformation) {
        customFileSource.setFile(info.getFile())
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
        loadIntoEditor(info.getFile(), info.getBoundingBox())
    }

    override fun loadIntoEditor(iid: Int) {
        loadIntoEditor(metaInfoCollector.getFile(iid), metaInfoCollector.getBounding(iid))
    }

    private fun loadIntoEditor(file: Foc, boundingBoxE6: BoundingBoxE6) {
        if (file.exists() && file.canWrite()) {
            clearEditor {
                editorSource.editor.unload()
                editorSource.edit(file)
                mapView.showEditor()
                frameInMap(boundingBoxE6)
                SolidOverlayFileEnabled(appContext.storage, InfoID.EDITOR_OVERLAY).value = true
            }
        }
    }

    override fun hideMap() {
        leaflet.visibleChild = stackPage.stackPage
    }

    override fun getName(infoID: Int): String {
        return InformationUtil.defaultName(infoID)
    }

    private fun setupDispatcher(dispatcher: Dispatcher) {
        dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(customFileSource)
        dispatcher.addOverlaySources(appContext, usageTrackers)
        dispatcher.addSource(FixedOverlaySource.createDraftSource(appContext, usageTrackers))
        dispatcher.addSource(FixedOverlaySource.createPoiSource(appContext, usageTrackers))
        dispatcher.addSource(editorSource)
        dispatcher.addTarget(metaInfoCollector, InfoID.ALL)
    }
}


private class MetaInfoCollector : TargetInterface {
    private val files = HashMap<Int, Foc>()
    private val boundings = HashMap<Int, BoundingBoxE6>()


    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        if (info.getFile().name.isNotEmpty()) {
            files[iid] = info.getFile()
        }

        if (info.getBoundingBox().hasBounding()) {
            boundings[iid] = info.getBoundingBox()
        }
    }

    fun hasBounding(iid: Int): Boolean {
       return  boundings.containsKey(iid)
    }

    fun getBounding(iid: Int): BoundingBoxE6 {
        return boundings.getOrDefault(iid, BoundingBoxE6.NULL_BOX)
    }

    fun getFile(iid: Int): Foc {
        return files.getOrDefault(iid, Foc.FOC_NULL)
    }
}
