package ch.bailu.aat_gtk.ui.view.solid

import ch.bailu.aat_lib.gpx.GpxInformation

interface ContextCallback {
    fun showInMap(info: GpxInformation)
    fun showDetail()
    fun showInList()
}