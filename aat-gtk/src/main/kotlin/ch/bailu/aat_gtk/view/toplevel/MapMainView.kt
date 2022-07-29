package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.*
import ch.bailu.aat_lib.map.edge.EdgeControlLayer
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OverlaySource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.selector.NodeSelectorLayer
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.gpx.GpxOverlayListLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.Window

class MapMainView(app: Application, dispatcher: DispatcherInterface, uiController: UiController, focFactory: FocFactory, window: Window): Attachable {

    val map = GtkCustomMapView(GtkAppContext, dispatcher)
    val overlay = Overlay()

    private val editorSource = EditorSource(GtkAppContext)

    private val navigationBar = NavigationBar(map.mContext, GtkAppContext.storage)
    private val infoBar = InfoBar(app, uiController, map.mContext, GtkAppContext.storage, focFactory, window)
    private val editorBar = EditorBar(app, map.mContext, GtkAppContext.services, editorSource)
    private val edgeControl = EdgeControlLayer(map.mContext, Bar.SIZE)

    init {
        dispatcher.addSource(editorSource)
        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.TRACKER))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.FILEVIEW))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.EDITOR_DRAFT))
        map.add(GpxOverlayListLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher))
        map.add(edgeControl)
        map.add(NodeSelectorLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext, Position.LEFT).apply {
            observe(editorBar)
            dispatcher.addTarget(this, InfoID.EDITOR_DRAFT)
            edgeControl.add(this)
        })

        map.add(NodeSelectorLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext, Position.RIGHT).apply {
            dispatcher.addTarget(this, InfoID.ALL)
            edgeControl.add(this)
        })

        overlay.child = map.drawingArea
        addBar(navigationBar)
        addBar(infoBar)
        addBar(editorBar)
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
}
