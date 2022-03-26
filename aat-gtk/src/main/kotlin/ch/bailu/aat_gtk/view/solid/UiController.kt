package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.gpx.GpxInformation

interface UiController {
    fun showMap()
    fun showInMap(info: GpxInformation)
    fun showCockpit()
    fun showDetail()
    fun showInList()
    fun showPreferencesMap()
}