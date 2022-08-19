package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

class MapQueryMenu(private val uiController: UiController) : MenuProvider {

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .label(ToDo.translate("Nominatim")) { println("Nominatim") }
            .label(ToDo.translate("Overpass")) { println("Opverpass selected") }
            .label(Res.str().p_mapsforge_poi()) { uiController.showPoi() }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return arrayOf()
    }
}
