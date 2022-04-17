package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.lib.menu.MenuModelBuilder
import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidOverlayFileList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gtk.Window

class MapMenu(
    private val uiController: UiController,
    private val mapContext: MapContext,
    mapDirectories: GtkMapDirectories,
    focFactory: FocFactory,
    window: Window

) : MenuProvider {

    private val srender = mapDirectories.createSolidRenderTheme()
    private val renderMenu = SolidFileSelectorMenu(srender, window)

    private val soverlay = SolidOverlayFileList(srender.storage, focFactory)
    private val overlayMenu = SolidCheckMenu(soverlay)

    private val soffline = mapDirectories.createSolidFile()
    private val offlineMenu = SolidFileSelectorMenu(soffline, window)

    private val stiles = SolidMapTileStack(srender)
    private val tilesMenu = SolidCheckMenu(stiles)

    override fun createMenu(): MenuModelBuilder {
        return MenuModelBuilder()
            .submenu(stiles.label, tilesMenu.createMenu())
            .submenu(soverlay.label, overlayMenu.createMenu())
            .submenu(soffline.label, offlineMenu.createMenu())
            .submenu(srender.label, renderMenu.createMenu())
            .label(Res.str().intro_settings()) {
                uiController.showPreferencesMap()
            }
            .label(Res.str().tt_info_reload()) {
                mapContext.mapView.reDownloadTiles()
            }
    }


    override fun createCustomWidgets(): Array<CustomWidget> {
        return tilesMenu.createCustomWidgets() +
                overlayMenu.createCustomWidgets() +
                offlineMenu.createCustomWidgets() +
                renderMenu.createCustomWidgets()
    }
}
