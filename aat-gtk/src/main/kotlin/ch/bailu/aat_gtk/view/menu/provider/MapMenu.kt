package ch.bailu.aat_gtk.view.menu.provider

import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.view.UiController
import ch.bailu.aat_gtk.view.menu.MenuHelper
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack
import ch.bailu.aat_lib.preferences.map.SolidCustomOverlayList
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc.FocFactory
import ch.bailu.gtk.gio.Menu
import ch.bailu.gtk.gtk.Application
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

    private val soverlay =
        SolidCustomOverlayList(
            srender.storage,
            focFactory
        )
    private val overlayMenu = SolidCheckMenu(soverlay)

    private val soffline = mapDirectories.createSolidFile()
    private val offlineMenu = SolidFileSelectorMenu(soffline, window)

    private val stiles = SolidMapTileStack(srender)
    private val tilesMenu = SolidCheckMenu(stiles)

    override fun createMenu(): Menu {
        return Menu().apply {
            appendSubmenu(stiles.label, tilesMenu.createMenu())
            appendSubmenu(soverlay.label, overlayMenu.createMenu())
            appendSubmenu(soffline.label, offlineMenu.createMenu())
            appendSubmenu(srender.label, renderMenu.createMenu())
            append(Res.str().intro_settings(), "app.showMapSettings")
            append(Res.str().tt_info_reload(), "app.reloadMapTiles")
        }
    }

    override fun createCustomWidgets(): Array<CustomWidget> {
        return tilesMenu.createCustomWidgets() +
                overlayMenu.createCustomWidgets() +
                offlineMenu.createCustomWidgets() +
                renderMenu.createCustomWidgets()
    }

    override fun createActions(app: Application) {
        MenuHelper.setAction(app, "showMapSettings") { uiController.showPreferencesMap() }
        MenuHelper.setAction(app, "reloadMapTiles") { mapContext.mapView.reDownloadTiles() }
        renderMenu.createActions(app)
        tilesMenu.createActions(app)
        overlayMenu.createActions(app)
        offlineMenu.createActions(app)
    }
}
