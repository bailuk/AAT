package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.app.exit
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.TrackerOverlayOnOffController
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.preferences.SolidWindowSize
import ch.bailu.aat_gtk.util.GtkTimer
import ch.bailu.aat_gtk.view.dialog.FileChangedDialog
import ch.bailu.aat_gtk.view.dialog.PreferencesDialog
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.menu.provider.LocationMenu
import ch.bailu.aat_gtk.view.messages.MessageOverlay
import ch.bailu.aat_gtk.view.search.PoiPage
import ch.bailu.aat_gtk.view.toplevel.navigation.NavigationView
import ch.bailu.aat_lib.Configuration
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.dispatcher.Dispatcher
import ch.bailu.aat_lib.dispatcher.TargetInterface
import ch.bailu.aat_lib.dispatcher.source.CurrentLocationSource
import ch.bailu.aat_lib.dispatcher.source.EditorSource
import ch.bailu.aat_lib.dispatcher.source.FileViewSource
import ch.bailu.aat_lib.dispatcher.source.FixedOverlaySource
import ch.bailu.aat_lib.dispatcher.source.IteratorSource
import ch.bailu.aat_lib.dispatcher.source.TrackerSource
import ch.bailu.aat_lib.dispatcher.source.TrackerTimerSource
import ch.bailu.aat_lib.dispatcher.source.addOverlaySources
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.preferences.map.overlay.SolidOverlayFileEnabled
import ch.bailu.aat_lib.preferences.map.SolidPositionLock
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.Foc
import ch.bailu.gtk.adw.Application
import ch.bailu.gtk.adw.ApplicationWindow
import ch.bailu.gtk.glib.Glib
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.lib.bridge.CSS
import org.mapsforge.core.model.LatLong

class MainWindow(private val app: Application, private val appContext: AppContext, dispatcher: Dispatcher) :
    UiControllerInterface {

    private val usageTrackers = UsageTrackers()
    private val editorSource = EditorSource(appContext, usageTrackers)
    private val customFileSource = FileViewSource(appContext, usageTrackers)
    private val summarySource = IteratorSource.Summary(appContext, usageTrackers)
    private val metaInfoCollector = MetaInfoCollector()

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
    private val navigationView = NavigationView(window)

    private val mainPage = MainPage(appContext, this, app, window, dispatcher, usageTrackers)

    private val poiView = PoiPage(appContext,this, app, window)
    private val mapView = MapMainView(app, appContext, dispatcher, usageTrackers, this, editorSource, window, navigationView).apply {
        overlay.setSizeRequest(Layout.MAP_WIN_WITH, Layout.WINDOW_MIN_SIZE)
        onAttached()
    }


    init {
        overlay.child = Box(Orientation.VERTICAL, 0).apply {
            append(navigationView.navigationSplitViewL1)
        }

        navigationView.setLeftSidebar(mainPage.layout, Configuration.appName)
        navigationView.setContent(mapView.overlay, Res.str().p_map())
        navigationView.setRightSidebar(poiView.layout, Res.str().p_mapsforge_poi())

        navigationView.observe(mainPage)
        navigationView.observe(poiView)

        setupDispatcher(dispatcher)
        TrackerOverlayOnOffController(appContext.storage, dispatcher)

        navigationView.showLeftSidebar()

        var blockCloseRequest = true

        window.onCloseRequest {
            if (blockCloseRequest) {
                clearEditor {
                    SolidWindowSize.writeSize(appContext.storage, window.width, window.height)
                    mainPage.stackView.save(appContext.storage)
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

        LocationMenu.createActions(appContext.storage, app, window.display, this)
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
        navigationView.showContent()
        mapView.showMainBar()

        // Flush UI
        while (Glib.mainContextDefault().iteration(false)) {}
    }

    override fun showPoi() {
        navigationView.showRightSidebar()
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

    override fun centerInMap(latLong: LatLong) {
        mapView.map.setCenter(latLong)
    }

    override fun centerInMap(iid: Int) {
        if (iid == InfoID.TRACKER) {
             SolidPositionLock(appContext.storage, GtkCustomMapView.DEFAULT_KEY).value = true
        } else if (metaInfoCollector.hasBounding(iid)) {
            mapView.map.setCenter(metaInfoCollector.getBounding(iid).center.toLatLong())
        }
    }

    override fun load(info: GpxInformation) {
        customFileSource.setFile(info.getFile())
    }

    override fun showCockpit() {
        navigationView.showLeftSidebar()
        mainPage.showCockpit()

    }

    override fun showDetail() {
        navigationView.showLeftSidebar()
        mainPage.showDetail()
    }

    override fun showPreferencesMap() {
        PreferencesDialog.showMap(app, appContext)
    }

    override fun getMapBounding(): BoundingBoxE6 {
        return BoundingBoxE6(mapView.map.boundingBox)
    }

    override fun showFileList() {
        hideMap()
        mainPage.showFileList()
    }

    override fun showPreferences() {
        PreferencesDialog.show(app, appContext)
    }

    override fun showInDetail(iid: Int) {
        mainPage.showInDetail(iid)
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
        navigationView.showLeftSidebar()
    }

    override fun getName(iid: Int): String {
        return InformationUtil.defaultName(iid)
    }

    override fun setOverlayEnabled(iid: Int, enabled: Boolean) {
        SolidOverlayFileEnabled(appContext.storage, iid).value = enabled
    }

    private fun setupDispatcher(dispatcher: Dispatcher) {
        dispatcher.addSource(TrackerTimerSource(GtkAppContext.services, GtkTimer()))
        dispatcher.addSource(CurrentLocationSource(GtkAppContext.services, GtkAppContext.broadcaster))
        dispatcher.addSource(TrackerSource(GtkAppContext.services, GtkAppContext.broadcaster, usageTrackers))
        dispatcher.addSource(customFileSource)
        dispatcher.addSource(summarySource)
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
