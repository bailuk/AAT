package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.controller.UiController
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.map.control.EditorBar
import ch.bailu.aat_gtk.view.map.control.InfoBar
import ch.bailu.aat_gtk.view.map.control.NavigationBar
import ch.bailu.aat_gtk.view.map.control.NodeInfo
import ch.bailu.aat_gtk.view.map.control.SearchBar
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.dispatcher.filter.ToggleFilter
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerInterface
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackers
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.edge.EdgeControlLayer
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
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
    editor: EditorSourceInterface,
    window: Window)
    : Attachable {

    val map = GtkCustomMapView(appContext, dispatcher)
    val overlay = Overlay()

    private val overlayList = ArrayList<OverlayContainer>().apply {
        val infoIDs = ArrayList<Int>().apply {
            add(InfoID.TRACKER)
            add(InfoID.POI)
            add(InfoID.EDITOR_OVERLAY)
            add(InfoID.EDITOR_DRAFT)
            add(InfoID.FILE_VIEW)
            for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
                add(InfoID.OVERLAY + i)
            }
        }.toIntArray()

        val usageTracker = usageTrackers.createOverlayUsageTracker(appContext.storage, *infoIDs)

        infoIDs.forEach {
            add(OverlayContainer(it, appContext, uiController, map, dispatcher, usageTracker))
        }
    }.toList()

    private val nodeInfo = NodeInfo()
    private val searchBar = SearchBar(uiController, app) {map.setCenter(it)}
    private val navigationBar = NavigationBar(map.getMContext(), appContext.storage, overlayList)
    private val infoBar = InfoBar(app, nodeInfo, uiController, map.getMContext(), appContext.storage, appContext, window)
    private val editorBar = EditorBar(app, nodeInfo, map.getMContext(), appContext.services, editor)
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

        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.getMContext(), dispatcher))
        map.add(GridDynLayer(appContext.services, appContext.storage, map.getMContext()))

        overlayList.forEach { map.add(it.gpxLayer) }

        map.add(edgeControl)
        map.add(NodeSelectorLayer(appContext.services, appContext.storage, map.getMContext(), Position.LEFT).apply {
            observe(editorBar)
            dispatcher.addTarget(this, InfoID.EDITOR_OVERLAY)
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

    fun showEditor() {
        edgeControl.show(Position.LEFT)
    }
}

private class OverlayContainer(
    val infoID: Int,
    private val appContext: AppContext,
    private val uiController: UiController,
    map: GtkCustomMapView,
    dispatcher: DispatcherInterface,
    usageTracker: UsageTrackerInterface): OverlayController {
    val gpxLayer = GpxDynLayer(appContext.storage, map.getMContext(), appContext.services)

    init {
        dispatcher.addTarget(ToggleFilter(gpxLayer, infoID, usageTracker))

    }

    override fun setEnabled(enabled: Boolean) {
        SolidOverlayFileEnabled(appContext.storage, infoID).value = enabled
    }

    override fun frame() {
        uiController.frameInMap(infoID)
    }

    override fun center() {
        uiController.centerInMap(infoID)
    }

    override fun getName(): String {
        return uiController.getName(infoID)
    }

    override fun isEnabled(): Boolean {
        return SolidOverlayFileEnabled(appContext.storage, infoID).value
    }
}
