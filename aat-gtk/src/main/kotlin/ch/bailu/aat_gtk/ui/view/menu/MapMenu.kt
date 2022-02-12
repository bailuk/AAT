package ch.bailu.aat_gtk.ui.view.menu

import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.ui.view.menu.model.LabelItem
import ch.bailu.aat_gtk.ui.view.menu.model.Menu
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory

class MapMenu(storageInterface: StorageInterface, focFactory: FocFactory) : Menu() {

    init {
        val mapDirs = GtkMapDirectories(storageInterface, focFactory)
        val smapFile: SolidMapsForgeMapFile = mapDirs.createSolidFile()
        val srenderTheme: SolidRenderTheme = mapDirs.createSolidRenderTheme()

        add(LabelItem(Res.str().p_map()) {println("Map Stack selected")})
        add(LabelItem(Res.str().p_overlay()) {println("Map Overlays selected")})
        add(LabelItem(smapFile.label) {println(smapFile.label)})
        add(LabelItem(srenderTheme.label) {println(srenderTheme.label)})
        add(LabelItem(Res.str().intro_settings()) {println("Map preferences selected")})
        add(LabelItem(Res.str().tt_info_reload()) {println("Map reload selected")})
    }
}