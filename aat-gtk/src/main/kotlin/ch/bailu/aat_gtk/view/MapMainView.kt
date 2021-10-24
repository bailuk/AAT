package ch.bailu.aat_gtk.view

import ch.bailu.aat_gtk.map.GtkCustomMapView
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.layer.gpx.GpxDynLayer
import ch.bailu.aat_lib.map.layer.grid.GridDynLayer
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.location.CurrentLocationLayer
import ch.bailu.aat_lib.service.ServicesInterface
import java.io.File
import java.util.ArrayList

class MapMainView(
    services: ServicesInterface,
    storage: StorageInterface,
    dispatcher: DispatcherInterface): Attachable {

    val map = GtkCustomMapView(storage, getMapFiles(), dispatcher)

    init {
        map.add(CurrentLocationLayer(map.mContext, dispatcher))
        map.add(GridDynLayer(services, storage, map.mContext))
        map.add(GpxDynLayer(storage, map.mContext, services, dispatcher, InfoID.TRACKER))
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