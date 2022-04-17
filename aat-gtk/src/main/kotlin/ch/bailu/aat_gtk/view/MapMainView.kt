package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.view.map.control.EditBar
import ch.bailu.aat_gtk.view.map.control.InfoBar
import ch.bailu.aat_gtk.view.map.control.MapBars
import ch.bailu.aat_gtk.view.map.control.NavigationBar
import ch.bailu.aat_gtk.view.map.layer.ControlBarLayer
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.gtk.Window
import java.io.File
import java.util.*

class MapMainView(app: Application, dispatcher: DispatcherInterface, uiController: UiController, focFactory: FocFactory, window: Window): Attachable {

    val map = GtkCustomMapView(GtkAppContext.storage, dispatcher)
    val overlay = Overlay()

    private val barControl = MapBars()
    private val navigationBar = NavigationBar(map.mContext, GtkAppContext.storage)
    private val infoBar = InfoBar(app, uiController, map.mContext, GtkAppContext.storage, focFactory, window)
    private val editBar = EditBar()

    init {

        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.TRACKER))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.FILEVIEW))
        map.add(ControlBarLayer(barControl))


        overlay.child = map.drawingArea
        overlay.addOverlay(barControl.add(navigationBar.bar.box, MapBars.BOTTOM))
        overlay.addOverlay(barControl.add(infoBar.bar.box, MapBars.RIGHT))
        overlay.addOverlay(barControl.add(editBar.bar.box, MapBars.LEFT))
    }


    private fun getMapFiles(): List<File> {
        val result = ArrayList<File>()
        val home = System.getProperty("user.home")
        result.add(File("${home}/maps/Alps/Alps_oam.osm.map"))
        return result
    }


    override fun onAttached() {
        map.onAttached()
    }

    override fun onDetached() {
        map.onDetached()
    }
}
