package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_lib.gpx.GpxInformation

interface UiController {
    fun showInMap(info: GpxInformation)
    fun showDetail()
    fun showInList()
    fun showPreferencesMap()
}