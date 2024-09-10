package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Icons
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.config.Strings
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.controller.OverlayControllerInterface
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.map.control.EditorBar
import ch.bailu.aat_gtk.view.map.control.EditorStatusLabel
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
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.InformationUtil
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.edge.EdgeControlLayer
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.map.layer.selector.NodeSelectorLayer
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
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
    uiController: UiControllerInterface,
    editor: EditorSourceInterface,
    window: Window)
    : Attachable {

    val map = GtkCustomMapView(appContext, dispatcher)
    val overlay = Overlay()

    private val overlayList = ArrayList<OverlayContainer>().apply {
        val infoIDs = InformationUtil.getMapOverlayInfoIdList().toIntArray()
        val usageTracker = usageTrackers.createOverlayUsageTracker(appContext.storage, *infoIDs)

        infoIDs.forEach {
            add(OverlayContainer(it, appContext, uiController, map, dispatcher, usageTracker))
        }
    }

    private val editableOverlayList = OverlayController.createEditableOverlayControllers(appContext.storage, uiController)

    private val nodeInfo = NodeInfo()
    private val statusLabel = EditorStatusLabel().apply {
        dispatcher.addTarget(this, InfoID.EDITOR_OVERLAY)
    }

    private val searchBar = SearchBar(uiController, app) {map.setCenter(it)}
    private val navigationBar = NavigationBar(map.getMContext(), appContext.storage, overlayList)
    private val infoBar = InfoBar(app, nodeInfo, uiController, map.getMContext(), appContext.storage, appContext, window)
    private val editorBar = EditorBar(app, nodeInfo, statusLabel, map.getMContext(), appContext.services, editor, editableOverlayList)
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
        overlay.addOverlay(statusLabel.box)
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
    iid: Int,
    appContext: AppContext,
    uiController: UiControllerInterface,
    map: GtkCustomMapView,
    dispatcher: DispatcherInterface,
    usageTracker: UsageTrackerInterface): OverlayControllerInterface {

    val overlayController = OverlayController(appContext.storage, uiController, iid)
    val gpxLayer = GpxDynLayer(appContext.storage, map.getMContext(), appContext.services)

    init {
        dispatcher.addTarget(ToggleFilter(gpxLayer, iid, usageTracker))

    }

    override fun setEnabled(enabled: Boolean) {
        overlayController.setEnabled(enabled)
    }

    override fun frame() {
        overlayController.frame()
    }

    override fun center() {
        overlayController.center()
    }

    override fun getName(): String {
        return overlayController.getName()
    }

    override fun isEnabled(): Boolean {
        return overlayController.isEnabled()
    }

    override fun showInDetail() {
        overlayController.showInDetail()
    }

    override fun edit() {
        overlayController.edit()
    }

    override fun isEditable(): Boolean {
        return overlayController.isEditable()
    }
}
