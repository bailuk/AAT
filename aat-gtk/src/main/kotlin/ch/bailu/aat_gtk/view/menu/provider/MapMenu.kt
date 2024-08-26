package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.controller.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window

class MapMenu(
    private val uiController: UiController,
    private val mapContext: MapContext,
    mapDirectories: GtkMapDirectories,
    window: Window

) : MenuProvider {

    private val srender = mapDirectories.createSolidRenderTheme()
    private val renderMenu = SolidFileSelectorMenu(srender, window)

    private val soffline = mapDirectories.createSolidFile()
    private val offlineMenu = SolidFileSelectorMenu(soffline, window)

    private val stiles = SolidMapTileStack(srender)
    private val tilesMenu = SolidCheckMenu(stiles)

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSubmenu(stiles.getLabel(), tilesMenu.createMenu())
            appendSubmenu(soffline.getLabel(), offlineMenu.createMenu())
            appendSubmenu(srender.getLabel(), renderMenu.createMenu())
            append(Res.str().intro_settings(), "app.showMapSettings")
            append(Res.str().tt_info_reload(), "app.reloadMapTiles")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return tilesMenu.createCustomWidgets() +
                offlineMenu.createCustomWidgets() +
                renderMenu.createCustomWidgets()
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "showMapSettings") { uiController.showPreferencesMap() }
        MenuHelper.setAction(app, "reloadMapTiles") { mapContext.getMapView().reDownloadTiles() }
        renderMenu.createActions(app)
        tilesMenu.createActions(app)
        offlineMenu.createActions(app)
    }
}
