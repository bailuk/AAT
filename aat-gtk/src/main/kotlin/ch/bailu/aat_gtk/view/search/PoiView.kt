package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.controller.UiControllerInterface
import ch.bailu.aat_gtk.util.extensions.margin
import ch.bailu.aat_gtk.view.solid.SolidDirectorySelectorView
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.search.poi.PoiApi
import ch.bailu.aat_lib.util.fs.AppDirectory
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Box
import ch.bailu.gtk.gtk.Editable
import ch.bailu.gtk.gtk.Orientation
import ch.bailu.gtk.gtk.SearchEntry
import ch.bailu.gtk.gtk.Separator
import ch.bailu.gtk.gtk.Window

class PoiView(private val controller: UiControllerInterface, app: Application, window: Window) {
    private val sdatabase = SolidPoiDatabase(GtkAppContext.mapDirectory, GtkAppContext)
    private val selected = AppDirectory.getDataDirectory(GtkAppContext.dataDirectory, AppDirectory.DIR_POI).child(AppDirectory.FILE_SELECTION)

    private val onPreferencesChanged = { _: StorageInterface, key: String ->
        if (sdatabase.hasKey(key)) {
            poiList.writeSelected()
            poiList.readList()
            updateList(Editable(searchEntry.cast()).text.toString())
        }
    }

    val poiApi = object: PoiApi(GtkAppContext) {
        override val selectedCategories
            get() = poiList.getSelectedCategories()
    }

    init {
        sdatabase.register(onPreferencesChanged)
    }

    private val searchEntry = SearchEntry().apply {
        onSearchChanged {
            updateList(Editable(cast()).text.toString())

        }
    }

    private val poiList = PoiList(sdatabase, selected) {
        if (it.isSummary()) {
            searchEntry.asEditable().setText(it.getSummaryKey())
        }
    }

    val layout = Box(Orientation.VERTICAL, Layout.MARGIN).apply {

        margin(Layout.MARGIN)

        append(SolidDirectorySelectorView(sdatabase, app, window).layout)
        append(Separator(Orientation.HORIZONTAL).apply {
            marginBottom = Layout.MARGIN*2
            marginTop = Layout.MARGIN*2
        })
        append(searchEntry)
        append(poiList.scrolled)
    }

    private fun updateList(text: String) {
        poiList.updateList(text)
    }

    fun loadList() {
        poiApi.startTask(GtkAppContext, controller.getMapBounding())
        poiList.writeSelected()
    }

    fun onDestroy() {
        sdatabase.unregister(onPreferencesChanged)
    }
}
