package ch.bailu.aat_gtk.view.toplevel

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_gtk.view.map.control.EditorBar
import ch.bailu.aat_gtk.view.map.control.InfoBar
import ch.bailu.aat_gtk.view.map.control.NavigationBar
import ch.bailu.aat_gtk.view.map.control.NodeInfo
import ch.bailu.aat_gtk.view.map.control.SearchBar
import ch.bailu.aat_lib.description.EditorSource
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OverlaySource
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
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Button
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.Window

class MapMainView(app: Application, dispatcher: DispatcherInterface, uiController: UiController, focFactory: FocFactory, window: Window): Attachable {

    val map = GtkCustomMapView(GtkAppContext, dispatcher)
    val overlay = Overlay()

    private val editorSource = EditorSource(GtkAppContext)
    private val overlayList = ArrayList<OverlaySource>().apply {
        val poiOverlaySource = OverlaySource.factoryPoiOverlaySource(GtkAppContext)
        add(poiOverlaySource)
        dispatcher.addSource(poiOverlaySource)

        for (i in 0 until SolidCustomOverlayList.MAX_OVERLAYS) {
            val overlaySource = OverlaySource.factoryCustomOverlaySource(GtkAppContext, i)
            add(overlaySource)
            dispatcher.addSource(overlaySource)
        }
    }

    private val nodeInfo = NodeInfo()
    private val searchBar = SearchBar(uiController, app) {map.setCenter(it)}
    private val navigationBar = NavigationBar(map.mContext, GtkAppContext.storage, overlayList, uiController)
    private val infoBar = InfoBar(app, nodeInfo, uiController, map.mContext, GtkAppContext.storage, focFactory, window)
    private val editorBar = EditorBar(app, nodeInfo, map.mContext, GtkAppContext.services, editorSource)
    private val edgeControl = EdgeControlLayer(map.mContext, Layout.barSize)

    init {
        overlay.addOverlay(Button().apply {
            setIconName("go-previous-symbolic")
            onClicked {
                uiController.hideMap()
            }
            halign = Align.END
            valign = Align.START
            margin(Layout.margin)
        })

        dispatcher.addSource(editorSource)
        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.TRACKER))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.FILEVIEW))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.EDITOR_DRAFT))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.EDITOR_OVERLAY))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.POI))

        map.add(GpxOverlayListLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher))
        map.add(edgeControl)
        map.add(NodeSelectorLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext, Position.LEFT).apply {
            observe(editorBar)
            dispatcher.addTarget(this, InfoID.ALL)
            edgeControl.add(this)
        })

        map.add(NodeSelectorLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext, Position.RIGHT).apply {
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
        editorSource.edit(info.file)
    }
}
