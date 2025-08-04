package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.OverlayController
import ch.bailu.aat_gtk.controller.OverlayControllerInterface
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.map.control.EditorBar
import ch.bailu.aat_gtk.view.map.control.EditorStatusLabel
import ch.bailu.aat_gtk.view.map.control.InfoBar
import ch.bailu.aat_gtk.view.map.control.MainBar
import ch.bailu.aat_gtk.view.map.control.NavigationBar
import ch.bailu.aat_gtk.view.map.control.NodeInfo
import ch.bailu.aat_gtk.view.toplevel.navigation.NavigationView
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface
import ch.bailu.aat_lib.dispatcher.filter.ToggleFilter
import ch.bailu.aat_lib.dispatcher.usage.UsageTrackerAlwaysEnabled
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
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.EventSequenceState
import ch.bailu.gtk.gtk.GestureSwipe
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.PropagationPhase
import ch.bailu.gtk.gtk.Window

class MapMainView(
    app: Application,
    appContext: AppContext,
    dispatcher: DispatcherInterface,
    usageTrackers: UsageTrackers,
    uiController: UiControllerInterface,
    editor: EditorSourceInterface,
    window: Window,
    navigationView: NavigationView
) : Attachable {

    val map = GtkCustomMapView(appContext, dispatcher)
    val box = Box(Orientation.VERTICAL, 0)
    val overlay = Overlay()

    private val infoIDs = InformationUtil.getMapOverlayInfoIdList().toIntArray()
    private val overlayUsageTracker = usageTrackers.createOverlayUsageTracker(appContext.storage, *infoIDs)

    private val overlayList = ArrayList<OverlayContainer>().apply {
        infoIDs.forEach {
            add(OverlayContainer(it, appContext, uiController, map, dispatcher, overlayUsageTracker))
        }
    }

    private val editableOverlayList =
        OverlayController.createEditableOverlayControllers(appContext.storage, uiController)

    private val nodeInfo = NodeInfo()
    private val statusLabel = EditorStatusLabel().apply {
        dispatcher.addTarget(this, InfoID.EDITOR_OVERLAY)
    }

    private val mainBar = MainBar(app, uiController, appContext.services, dispatcher)
    private val navigationBar = NavigationBar(map.getMContext(), appContext.storage, overlayList)
    private val infoBar = InfoBar(
        app,
        nodeInfo,
        uiController,
        map.getMContext(),
        appContext.storage,
        appContext,
        window
    )
    private val editorBar = EditorBar(
        app,
        nodeInfo,
        statusLabel,
        map.getMContext(),
        appContext.services,
        editor,
        editableOverlayList
    )
    private val edgeControl = EdgeControlLayer(map.getMContext(), Layout.BAR_SIZE)

    init {
        navigationView.observe(mainBar)
        overlay.addController(GestureSwipe().apply {
            // This is to prevent propagation of events to NavigationSplitView
            // TODO Consider adding this to MapView
            onSwipe { _, _ ->
                setState(EventSequenceState.CLAIMED)

            }
            propagationPhase = PropagationPhase.BUBBLE
        })
        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.getMContext(), dispatcher))
        map.add(GridDynLayer(appContext.services, appContext.storage, map.getMContext()))

        overlayList.forEach { map.add(it.gpxLayer) }

        map.add(edgeControl)
        map.add(
            NodeSelectorLayer(
                appContext.services,
                appContext.storage,
                map.getMContext(),
                Position.LEFT,
                UsageTrackerAlwaysEnabled()
            ).apply {
                observe(editorBar)
                dispatcher.addTarget(this, InfoID.EDITOR_OVERLAY)
                edgeControl.add(this)
            })

        map.add(
            NodeSelectorLayer(
                appContext.services,
                appContext.storage,
                map.getMContext(),
                Position.RIGHT,
                overlayUsageTracker
            ).apply {
                observe(infoBar)
                dispatcher.addTarget(this, InfoID.ALL)
                edgeControl.add(this)
            })

        overlay.child = map.drawingArea
        addBar(mainBar)
        addBar(navigationBar)
        addBar(infoBar)
        addBar(editorBar)

        overlay.addOverlay(nodeInfo.box)
        overlay.addOverlay(statusLabel.box)

        showMainBar()
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

    fun showMainBar() {
        edgeControl.show(Position.TOP)
    }
}

private class OverlayContainer(
    iid: Int,
    appContext: AppContext,
    uiController: UiControllerInterface,
    map: GtkCustomMapView,
    dispatcher: DispatcherInterface,
    usageTracker: UsageTrackerInterface
) : OverlayControllerInterface {

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
