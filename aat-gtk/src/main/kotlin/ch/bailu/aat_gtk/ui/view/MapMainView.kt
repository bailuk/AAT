package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.ui.view.map.GtkCustomMapView
import ch.bailu.aat_gtk.ui.view.map.control.BarControl
import ch.bailu.aat_gtk.ui.view.map.control.InfoBar
import ch.bailu.aat_gtk.ui.view.map.control.NavigationBar
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.gtk.gtk.Align
import ch.bailu.gtk.gtk.Overlay
import ch.bailu.gtk.helper.ActionHelper
import java.io.File
import java.util.*

class MapMainView(actionHelper: ActionHelper, dispatcher: DispatcherInterface): Attachable {

    val map = GtkCustomMapView(GtkAppContext.storage, getMapFiles(), dispatcher)
    val overlay = Overlay()

    private val barControl = BarControl()
    private val navigationBar = NavigationBar(actionHelper, map.mContext, GtkAppContext.storage)
    private val infoBar = InfoBar()

    init {

        dispatcher.addTarget(navigationBar, InfoID.ALL)

        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(GtkAppContext.services, GtkAppContext.storage, map.mContext))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.TRACKER))
        map.add(GpxDynLayer(GtkAppContext.storage, map.mContext, GtkAppContext.services, dispatcher, InfoID.FILEVIEW))


        overlay.child = map.drawingArea
        overlay.addOverlay(barControl.add(navigationBar.box, BarControl.BOTTOM))
        overlay.addOverlay(barControl.add(infoBar.box, BarControl.LEFT))
        barControl.show(BarControl.LEFT)
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
