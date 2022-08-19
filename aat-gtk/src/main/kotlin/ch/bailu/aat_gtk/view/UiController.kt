package ch.bailu.aat_gtk.view

import ch.bailu.aat_lib.coordinates.BoundingBoxE6
import ch.bailu.aat_lib.gpx.GpxInformation

interface UiController {
    fun showMap()
    fun showPoi()
    fun showInMap(info: GpxInformation)
    fun load(info: GpxInformation)
    fun showCockpit()
    fun showDetail()
    fun showInList()
    fun showPreferencesMap()
    fun back()
    fun showContextBar()
    fun getMapBounding(): BoundingBoxE6
}
