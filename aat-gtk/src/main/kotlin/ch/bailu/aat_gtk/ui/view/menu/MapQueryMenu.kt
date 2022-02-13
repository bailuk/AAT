package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.ui.view.menu.model.FixedLabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo

class MapQueryMenu : Menu() {

    init {
        add(FixedLabelItem(ToDo.translate("Nominatim")) {println("Nominatim")})
        add(FixedLabelItem(ToDo.translate("Overpass")) {println("Opverpass selected")})
        add(FixedLabelItem(Res.str().p_mapsforge_poi()) {println("POI selected")})
    }
}
