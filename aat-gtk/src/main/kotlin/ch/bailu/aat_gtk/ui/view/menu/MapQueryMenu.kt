package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

class MapQueryMenu : Menu() {

    init {

        add(LabelItem(ToDo.translate("Nominatim")) {println("Nominatim")})
        add(LabelItem(ToDo.translate("Overpass")) {println("Opverpass selected")})
        add(LabelItem(Res.str().p_mapsforge_poi()) {println("POI selected")})
    }
}
