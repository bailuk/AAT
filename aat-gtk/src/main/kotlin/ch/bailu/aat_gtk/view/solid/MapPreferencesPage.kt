package ch.bailu.aat_gtk.view.solid

import ch.bailu.aat_gtk.solid.GtkMapDirectories
import ch.bailu.aat_gtk.solid.GtkSolidDem3Directory
import ch.bailu.aat_gtk.solid.GtkSolidTileCacheDirectory
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache
import ch.bailu.aat_lib.preferences.map.SolidLayerType
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectoryHint
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeMapFile
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.map.SolidScaleFactor
import ch.bailu.aat_lib.resources.Res
import ch.bailu.gtk.adw.PreferencesGroup
import ch.bailu.gtk.gtk.Application
import ch.bailu.gtk.gtk.Window

class MapPreferencesPage(appContext: AppContext, app: Application, window: Window) : PreferencesPageParent(Res.str().p_map(), "map") {

    init {
        page.add(PreferencesGroup().apply {
            setTitle(Res.str().p_map())
            add(SolidDirectorySelectorView(GtkSolidTileCacheDirectory(appContext.storage, appContext) , app, window).layout)
            add(SolidIndexComboRowView(SolidLayerType(appContext.storage)).layout)
        })

        page.add(PreferencesGroup().apply {
            val solidMapDirectory = SolidMapsForgeDirectoryHint(appContext.storage, appContext, GtkMapDirectories(appContext.storage, appContext))
            setTitle(Res.str().p_offline_map())
            add(SolidDirectorySelectorView(solidMapDirectory, app, window).layout)
            add(SolidDirectorySelectorView(SolidMapsForgeMapFile(solidMapDirectory, appContext), app, window).layout)
            add(SolidDirectorySelectorView(SolidRenderTheme(solidMapDirectory, appContext), app, window).layout)
            add(SolidIndexComboRowView(SolidScaleFactor(appContext.storage)).layout)
            add(SolidBooleanSwitchView(SolidEnableTileCache.MapsForge(appContext.storage)).layout)
        })

        page.add(PreferencesGroup().apply {
            setTitle(Res.str().p_dem())
            add(SolidDirectorySelectorView(GtkSolidDem3Directory(appContext.storage, appContext), app, window).layout)
            add(SolidBooleanSwitchView(SolidDem3EnableDownload(appContext.storage)).layout)
        })

        page.add(PreferencesGroup().apply {
            setTitle(ElevationSource.ELEVATION_HILLSHADE.name)
            add(SolidBooleanSwitchView(SolidEnableTileCache.HillShade(appContext.storage)).layout)
        })
    }
}
