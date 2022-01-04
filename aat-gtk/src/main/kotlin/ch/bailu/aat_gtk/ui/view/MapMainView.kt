package ch.bailu.aat_gtk.ui.view

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.ui.view.map.GtkCustomMapView
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.IteratorSource
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.helper.ActionHelper
import java.io.File
import java.util.*

class MapMainView(
        actionHelper: ActionHelper,
        services: ServicesInterface,
        storage: StorageInterface,
        dispatcher: DispatcherInterface): Attachable {

    val map = GtkCustomMapView(storage, getMapFiles(), dispatcher)
    val layout = Box(Orientation.VERTICAL,0)
    val bar = NavigationBar(actionHelper, map.mContext, storage)

    init {
        layout.append(map.drawingArea)
        layout.append(bar.box)

        dispatcher.addSource(IteratorSource.FollowFile(GtkAppContext))

        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(services, storage, map.mContext))
        map.add(GpxDynLayer(storage, map.mContext, services, dispatcher, InfoID.TRACKER))
        map.add(GpxDynLayer(storage, map.mContext, services, dispatcher, InfoID.FILEVIEW))

        layout.show()
        dispatcher.addTarget(bar, InfoID.ALL)
    }


    private fun getMapFiles(): List<File> {
        val result: MutableList<File> = ArrayList(2)
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