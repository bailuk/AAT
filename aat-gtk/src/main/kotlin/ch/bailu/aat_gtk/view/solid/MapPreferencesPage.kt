package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppDensity
import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.solid.GtkSolidDem3Directory
import ch.bailu.aat_gtk.solid.GtkSolidTileCacheDirectory
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidLayerType
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.map.SolidScaleFactor
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application

class MapPreferencesPage(appContext: AppContext, app: Application, window: PreferencesWindow) : PreferencesPageParent(Res.str().p_map(), "map") {

    init {
        add(Res.str().p_map())

        val solidMapDirectory = SolidMapsForgeDirectory(appContext.storage, appContext, GtkMapDirectories(appContext.storage, appContext))

        add(SolidIndexComboView(SolidTileSize(appContext.storage, GtkAppDensity())).layout)

        add(SolidDirectorySelectorView(GtkSolidTileCacheDirectory(appContext.storage, appContext) , app, window).layout)
        add(SolidIndexComboView(SolidLayerType(appContext.storage)).layout)

        add(Res.str().p_offline_map())
        add(SolidDirectorySelectorView(solidMapDirectory, app, window).layout)
        add(SolidDirectorySelectorView(SolidMapsForgeMapFile(appContext.storage, appContext, GtkMapDirectories(appContext.storage, appContext)), app, window).layout)
        add(SolidDirectorySelectorView(SolidRenderTheme(solidMapDirectory, appContext), app, window).layout)
        add(SolidIndexComboView(SolidScaleFactor(appContext.storage)).layout)
        add(SolidBooleanSwitchView(SolidEnableTileCache.MapsForge(appContext.storage)).layout)

        add(Res.str().p_dem())
        add(SolidDirectorySelectorView(GtkSolidDem3Directory(appContext.storage, appContext), app, window).layout)
        add(SolidBooleanSwitchView(SolidDem3EnableDownload(appContext.storage)).layout)

        add(ElevationSource.ELEVATION_HILLSHADE.name)
        add(SolidBooleanSwitchView(SolidEnableTileCache.HillShade(appContext.storage)).layout)
    }
}
