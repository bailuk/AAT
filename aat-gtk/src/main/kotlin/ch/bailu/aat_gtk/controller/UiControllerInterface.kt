package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.information.GpxInformation
import org.mapsforge.core.model.LatLong

interface UiControllerInterface {
    fun showMap()
    fun showPoi()
    fun frameInMap(info: GpxInformation)
    fun frameInMap(iid: Int)
    fun centerInMap(info: GpxInformation)
    fun centerInMap(latLong: LatLong)
    fun centerInMap(iid: Int)
    fun load(info: GpxInformation)
    fun showCockpit()
    fun showDetail()
    fun showPreferencesMap()
    fun getMapBounding(): BoundingBoxE6
    fun showFileList()
    fun showPreferences()
    fun showInDetail(iid: Int)
    fun loadIntoEditor(info: GpxInformation)
    fun loadIntoEditor(iid: Int)
    fun hideMap()
    fun getName(iid: Int): String
    fun setOverlayEnabled(iid: Int, enabled: Boolean)
}
