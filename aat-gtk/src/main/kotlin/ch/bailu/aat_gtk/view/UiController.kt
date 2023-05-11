package ch.bailu.aat_gtk.view

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.GpxInformation

interface UiController {
    fun showMap()
    fun showPoi()
    fun frameInMap(info: GpxInformation)
    fun centerInMap(info: GpxInformation)
    fun load(info: GpxInformation)
    fun showCockpit()
    fun showDetail()
    fun showInList()
    fun showPreferencesMap()
    fun back()
    fun showContextBar()
    fun getMapBounding(): BoundingBoxE6
    fun showFileList()
    fun showPreferences()
    fun showInDetail(infoID: Int)
}
