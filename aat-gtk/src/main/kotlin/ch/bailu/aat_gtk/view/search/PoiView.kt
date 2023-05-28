package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.solid.SolidDirectorySelectorView
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.search.poi.PoiApi
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Editable
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.SearchEntry
import ch.bailu.gtk.gtk.Separator
import ch.bailu.gtk.gtk.Window
import org.mapsforge.poi.storage.PoiCategory

class PoiView(private val controller: UiController, app: Application, window: Window) {
    private val sdatabase = SolidPoiDatabase(GtkAppContext.mapDirectory, GtkAppContext)
    private val selected = AppDirectory.getDataDirectory(GtkAppContext.dataDirectory, AppDirectory.DIR_POI).child(PoiApi.SELECTED)

    private val searchEntry = SearchEntry().apply {
        onSearchChanged {
            updateList(Editable(cast()).text.toString())
        }
    }

    private val poiList = PoiList(sdatabase, selected) {
        if (it.isSummary) {
            searchEntry.asEditable().setText(it.summaryKey)
        }
    }

    val layout = Box(Orientation.VERTICAL, Layout.margin).apply {

        margin(Layout.margin)

        append(SolidDirectorySelectorView(sdatabase, app, window).layout)
        append(Separator(Orientation.HORIZONTAL).apply {
            marginBottom = Layout.margin*2
            marginTop = Layout.margin*2
        })
        append(searchEntry)
        append(poiList.scrolled)
    }

    private fun updateList(text: String) {
        poiList.updateList(text)
    }

    private fun getSelectedCategoriesFromList(): ArrayList<PoiCategory> {
        return poiList.getSelectedCategories()
    }


    fun loadList() {
        val poiApi = object: PoiApi(GtkAppContext, controller.getMapBounding()) {
            override fun getSelectedCategories(): ArrayList<PoiCategory> {
                return getSelectedCategoriesFromList()
            }
        }

        poiApi.startTask(GtkAppContext)
        val overlay = SolidOverlayFileList(GtkAppContext.storage, GtkAppContext).get(SolidOverlayFileList.MAX_OVERLAYS-1)
        overlay.setValueFromFile(poiApi.resultFile)
        overlay.isEnabled = true
    }
}
