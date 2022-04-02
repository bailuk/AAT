package ch.bailu.aat_gtk.view.menu

import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.view.menu.model.FixedLabelItem
import ch.bailu.aat_gtk.view.menu.model.Menu
import ch.bailu.aat_gtk.view.menu.model.SubmenuItem
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory

class MapMenu(storageInterface: StorageInterface, focFactory: FocFactory, uiController: UiController, mapContext: MapContext) : Menu() {

    init {
        add(
            SubmenuItem(
                SolidCheckMenu(
                    SolidMapTileStack(
                        GtkMapDirectories(storageInterface, focFactory).createSolidRenderTheme()
                    )
                )
            )
        )

        add(
            SubmenuItem(
                SolidCheckMenu(
                    SolidOverlayFileList(storageInterface, focFactory)
                )
            )
        )
        add(
            SubmenuItem(
                SolidFileListMenu(
                    GtkMapDirectories(storageInterface, focFactory).createSolidFile()
                )
            )
        )

        add(
            SubmenuItem(
                SolidFileListMenu(
                    GtkMapDirectories(storageInterface, focFactory).createSolidRenderTheme()
                )
            )
        )
        add(FixedLabelItem(Res.str().intro_settings()) {
            uiController.showPreferencesMap()
        })
        add(FixedLabelItem(Res.str().tt_info_reload()) {
            mapContext.mapView.reDownloadTiles()
        })
    }
}