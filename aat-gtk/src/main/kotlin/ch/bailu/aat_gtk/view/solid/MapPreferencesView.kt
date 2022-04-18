package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.app.GtkAppConfig
import ch.bailu.aat_gtk.app.GtkAppContext
import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.solid.GtkSolidDem3Directory
import ch.bailu.aat_gtk.solid.GtkSolidTileCacheDirectory
import ch.bailu.aat_gtk.view.VerticalScrollView
import ch.bailu.aat_lib.preferences.SolidVolumeKeys
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.*
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache.MapsForge
import ch.bailu.aat_lib.resources.Res
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window

class MapPreferencesView (storage: StorageInterface, app: Application, window: Window) : VerticalScrollView() {
    init {
        add(Res.str().p_map())

        val solidMapDirectory = SolidMapsForgeDirectory(storage, GtkAppContext, GtkMapDirectories(storage, GtkAppContext))

        // TODO there is no density support yet
        add(SolidIndexComboView(SolidTileSize(storage, GtkAppConfig.density)).layout)

        add(SolidDirectorySelectorView(GtkSolidTileCacheDirectory(storage, GtkAppContext) , app, window).layout)
        add(SolidBooleanSwitchView(SolidVolumeKeys(storage)).layout)

        add(Res.str().p_offline_map())
        add(SolidDirectorySelectorView(solidMapDirectory, app, window).layout)
        add(SolidDirectorySelectorView(SolidMapsForgeMapFile(storage, GtkAppContext, GtkMapDirectories(storage, GtkAppContext)), app, window).layout)
        add(SolidDirectorySelectorView(SolidRenderTheme(solidMapDirectory, GtkAppContext), app, window).layout)
        add(SolidBooleanSwitchView(MapsForge(storage)).layout)

        add(ToDo.translate("Dem3 altitude tiles"))
        add(SolidDirectorySelectorView(GtkSolidDem3Directory(storage, GtkAppContext), app, window).layout)
        add(SolidBooleanSwitchView(SolidDem3EnableDownload(storage)).layout)
    }
}
