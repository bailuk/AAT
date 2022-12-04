package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application

class MapQueryMenu(private val uiController: UiController) : MenuProvider {

    override fun createMenu(): Menu {
        return Menu().apply {
            append(ToDo.translate("Nominatim"), "app.queryNominatim")
            append(ToDo.translate("Overpass"), "app.queryOverpass")
            append(Res.str().p_mapsforge_poi(), "app.queryOfflinePoi")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "queryOfflinePoi") { uiController.showPoi() }
    }
}
