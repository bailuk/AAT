package ch.bailu.aat_gtk.controller

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.information.GpxInformation

interface UiControllerInterface {
    fun showMap()
    fun showPoi()
    fun frameInMap(info: GpxInformation)
    fun frameInMap(infoID: Int)
    fun centerInMap(info: GpxInformation)
    fun centerInMap(infoID: Int)
    fun load(info: GpxInformation)
    fun showCockpit()
    fun showDetail()
    fun showInList()
    fun showPreferencesMap()
    fun getMapBounding(): BoundingBoxE6
    fun showFileList()
    fun showPreferences()
    fun showInDetail(infoID: Int)
    fun loadIntoEditor(info: GpxInformation)
    fun loadIntoEditor(iid: Int)
    fun hideMap()
    fun getName(infoID: Int): String
}
