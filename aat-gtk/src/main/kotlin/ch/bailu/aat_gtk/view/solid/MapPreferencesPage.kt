package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.app.GtkAppDensity
import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.solid.GtkSolidDem3Directory
import ch.bailu.aat_gtk.solid.GtkSolidTileCacheDirectory
import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.preferences.SolidVolumeKeys
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.map.SolidScaleFactor
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.adw.PreferencesWindow
import ch.bailu.gtk.gtk.Application

class MapPreferencesPage(storage: StorageInterface, app: Application, window: PreferencesWindow) : PreferencesPageParent(Res.str().p_map(), "map") {

    init {
        add(Res.str().p_map())

        val solidMapDirectory = SolidMapsForgeDirectory(storage, GtkAppContext, GtkMapDirectories(storage, GtkAppContext))


        add(SolidIndexComboView(SolidTileSize(storage, GtkAppDensity())).layout)

        add(SolidDirectorySelectorView(GtkSolidTileCacheDirectory(storage, GtkAppContext) , app, window).layout)
        add(SolidBooleanSwitchView(SolidVolumeKeys(storage)).layout)

        add(Res.str().p_offline_map())
        add(SolidDirectorySelectorView(solidMapDirectory, app, window).layout)
        add(SolidDirectorySelectorView(SolidMapsForgeMapFile(storage, GtkAppContext, GtkMapDirectories(storage, GtkAppContext)), app, window).layout)
        add(SolidDirectorySelectorView(SolidRenderTheme(solidMapDirectory, GtkAppContext), app, window).layout)
        add(SolidIndexComboView(SolidScaleFactor(storage)).layout)
        add(SolidBooleanSwitchView(SolidEnableTileCache.MapsForge(storage)).layout)

        add(ToDo.translate("Dem3 altitude tiles"))
        add(SolidDirectorySelectorView(GtkSolidDem3Directory(storage, GtkAppContext), app, window).layout)
        add(SolidBooleanSwitchView(SolidDem3EnableDownload(storage)).layout)

        add(ElevationSource.ELEVATION_HILLSHADE.name)
        add(SolidBooleanSwitchView(SolidEnableTileCache.Hillshade(storage)).layout)
    }
}
