package ch.bailu.aat_gtk.view.search

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.config.Layout
import ch.bailu.aat_gtk.lib.extensions.margin
import ch.bailu.aat_gtk.lib.extensions.setText
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.solid.SolidDirectorySelectorView
import ch.bailu.aat_lib.preferences.SolidPoiDatabase
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.search.poi.PoiApi
import ch.bailu.foc.FocFile
import ch.bailu.gtk.gtk.*
import ch.bailu.gtk.type.Str
import org.mapsforge.poi.storage.PoiCategory

class PoiStackView(private val controller: UiController, app: Application, window: Window) {
    private val sdatabase = SolidPoiDatabase(GtkAppContext.mapDirectory, GtkAppContext)

    private val searchEntry = SearchEntry().apply {
        onSearchChanged {
            updateList(Editable(cast()).text.toString())
        }
    }

    private val poiList = PoiList(sdatabase, FocFile("test")) {
        if (it.isSummary) {
            Editable(searchEntry.cast()).setText(it.summaryKey)
        } else {
            it.select()
            updateList()
        }
    }
    val layout = Box(Orientation.VERTICAL, Layout.margin).apply {

        margin(Layout.margin)
        val buttonBox = Box(Orientation.HORIZONTAL, Layout.margin)
        buttonBox.append(
            Button.newWithLabelButton(Str(ToDo.translate("Back"))).apply {
                onClicked { controller.back() }
            }
        )
        buttonBox.append(Button.newWithLabelButton(Str(ToDo.translate("Load"))).apply {
            onClicked { loadList() }
        })

        append(buttonBox)
        append(SolidDirectorySelectorView(sdatabase, app, window).layout)
        append(searchEntry)
        append(poiList.scrolled)
    }

    private fun updateList() {
        poiList.updateList()
    }

    private fun updateList(text: String) {
        poiList.updateList(text)
    }

    private fun getSelectedCategoriesFromList(): ArrayList<PoiCategory> {
        return poiList.getSelectedCategories()
    }


    private fun loadList() {
        val poiApi = object: PoiApi(GtkAppContext, controller.getMapBounding()) {
            override fun getSelectedCategories(): ArrayList<PoiCategory> {
                return getSelectedCategoriesFromList()
            }
        }

        poiApi.startTask(GtkAppContext)
        val overlay = SolidOverlayFileList(GtkAppContext.storage, GtkAppContext).get(SolidOverlayFileList.MAX_OVERLAYS-1)
        overlay.setValueFromFile(poiApi.resultFile)
        overlay.isEnabled = true
        controller.back()
    }
}
