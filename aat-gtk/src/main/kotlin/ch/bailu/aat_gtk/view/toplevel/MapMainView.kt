package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.controller.UiController
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.map.control.EditorBar
import ch.bailu.aat_gtk.view.map.control.InfoBar
import ch.bailu.aat_gtk.view.map.control.NavigationBar
import ch.bailu.aat_gtk.view.map.control.NodeInfo
import ch.bailu.aat_gtk.view.map.control.SearchBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.EditorSource
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.filter.Filter
import ch.bailu.aat_lib.dispatcher.source.FileSource
import ch.bailu.aat_lib.dispatcher.usage.OverlayUsageTracker
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.edge.EdgeControlLayer
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.gpx.GpxOverlayListLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.map.layer.selector.NodeSelectorLayer
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileEnabled
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.Window

class MapMainView(
    app: Application,
    appContext: AppContext,
    dispatcher: DispatcherInterface,
    usageTrackers: UsageTrackers,
    uiController: UiController,
    window: Window)
    : Attachable {

    val map = GtkCustomMapView(appContext, dispatcher)
    val overlay = Overlay()

    private val editorSource = EditorSource(appContext)
    private val overlayList = ArrayList<OverlayContainer>().apply {

        val iids = arrayOf(
            InfoID.TRACKER,
            InfoID.POI,
            InfoID.EDITOR_DRAFT, // TODO should be InfoID.DRAFT
            InfoID.FILE_VIEW)

        val usageTracker = usageTrackers.createOverlayUsageTracker(appContext.storage)
        for (i in iids) {
            add(OverlayContainer(i, appContext, map, dispatcher, usageTracker))
            dispatcher.addSource(FileSource(appContext, i, usageTrackers))
        }

        for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
            add(OverlayContainer(i, appContext, map, dispatcher, usageTracker))
            dispatcher.addSource(FileSource(appContext, i, usageTrackers))
        }
    }

    private val nodeInfo = NodeInfo()
    private val searchBar = SearchBar(uiController, app) {map.setCenter(it)}
    private val navigationBar = NavigationBar(map.getMContext(), appContext.storage, overlayList)
    private val infoBar = InfoBar(app, nodeInfo, uiController, map.getMContext(), appContext.storage, appContext, window)
    private val editorBar = EditorBar(app, nodeInfo, map.getMContext(), appContext.services, editorSource)
    private val edgeControl = EdgeControlLayer(map.getMContext(), Layout.barSize)

    init {
        overlay.addOverlay(Button().apply {
            addCssClass(Strings.mapControl)
            iconName = Icons.goPreviousSymbolic
            onClicked {
                uiController.hideMap()
            }
            halign = Align.END
            valign = Align.START
            margin(Layout.margin)
        })

        dispatcher.addSource(editorSource)
        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.getMContext(), dispatcher))
        map.add(GridDynLayer(appContext.services, appContext.storage, map.getMContext()))

        overlayList.forEach { map.add(it.gpxLayer) }

        map.add(GpxOverlayListLayer(appContext.storage, map.getMContext(), appContext.services, dispatcher))
        map.add(edgeControl)
        map.add(NodeSelectorLayer(appContext.services, appContext.storage, map.getMContext(), Position.LEFT).apply {
            observe(editorBar)
            dispatcher.addTarget(this, InfoID.ALL)
            edgeControl.add(this)
        })

        map.add(NodeSelectorLayer(appContext.services, appContext.storage, map.getMContext(), Position.RIGHT).apply {
            observe(infoBar)
            dispatcher.addTarget(this, InfoID.ALL)
            edgeControl.add(this)
        })

        overlay.child = map.drawingArea
        addBar(searchBar)
        addBar(navigationBar)
        addBar(infoBar)
        addBar(editorBar)

        overlay.addOverlay(nodeInfo.box)
    }

    private fun addBar(bar: Bar) {
        edgeControl.add(bar)
        overlay.addOverlay(bar.box)
    }

    override fun onAttached() {
        map.onAttached()
    }

    override fun onDetached() {
        map.onDetached()
    }

    fun editDraft() {
        editorSource.editDraft()
    }

    fun edit(info: GpxInformation) {
        edgeControl.show(Position.LEFT)
        editorSource.edit(info.getFile())
    }
}

private class OverlayContainer(
    val infoID: Int,
    private val appContext: AppContext,
    map: GtkCustomMapView,
    dispatcher: DispatcherInterface,
    usageTracker: UsageTrackerInterface): OverlayController {
    val gpxLayer = GpxDynLayer(appContext.storage, map.getMContext(), appContext.services)



    init {
        dispatcher.addTarget(Filter(gpxLayer, usageTracker))

    }

    override fun setEnabled(enabled: Boolean) {
        SolidOverlayFileEnabled(appContext.storage, infoID).value = enabled
    }

    override fun frame() {
        TODO("Not yet implemented")
    }

    override fun center() {
        TODO("Not yet implemented")
    }

    override fun getName(): String {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        return SolidOverlayFileEnabled(appContext.storage, infoID).value
    }

}
