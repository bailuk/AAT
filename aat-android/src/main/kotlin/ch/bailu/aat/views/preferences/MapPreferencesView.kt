package ch.bailu.aat.views.preferences

import android.app.Activity
import ch.bailu.aat.R
import ch.bailu.aat.preferences.Storage
import ch.bailu.aat.preferences.map.AndroidMapDirectories
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.util.ui.AndroidAppDensity
import ch.bailu.aat.util.ui.theme.UiTheme
import ch.bailu.aat.views.tileremover.TileRemoverView
import ch.bailu.aat_lib.map.tile.source.ElevationSource
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource
import ch.bailu.aat_lib.preferences.SolidVolumeKeys
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache.HillShade
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache.MapsForge
import ch.bailu.aat_lib.preferences.map.SolidLayerType
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme
import ch.bailu.aat_lib.preferences.map.SolidScaleFactor
import ch.bailu.aat_lib.preferences.map.SolidTileSize
import ch.bailu.aat_lib.preferences.map.SolidTrimDate
import ch.bailu.aat_lib.preferences.map.SolidTrimMode
import ch.bailu.aat_lib.preferences.map.SolidTrimSize
import ch.bailu.aat_lib.resources.Res
import ch.bailu.foc_android.FocAndroidFactory

class MapPreferencesView(acontext: Activity, scontext: ServiceContext, theme: UiTheme) :
    VerticalScrollView(scontext.getContext()) {
    private val tileRemover: TileRemoverView

    init {
        val context = scontext.getContext()
        val storage: StorageInterface = Storage(context)
        val solidMapDirectory = AndroidMapDirectories(acontext).createSolidDirectory()
        val solidMapFile = AndroidMapDirectories(acontext).createSolidFile()

        add(TitleView(context, context.getString(R.string.p_tiles), theme))
        add(SolidIndexListView(context, SolidTileSize(storage, AndroidAppDensity(context)), theme))
        add(SolidDirectoryViewSAF(acontext, AndroidSolidTileCacheDirectory(context), theme))
        add(SolidCheckBox(acontext, SolidVolumeKeys(storage), theme))
        add(SolidIndexListView(acontext, SolidLayerType(storage), theme))
        add(TitleView(context, MapsForgeSource.NAME, theme))
        add(SolidStringView(context, solidMapDirectory, theme))
        add(SolidStringView(context, solidMapFile, theme))
        add(
            SolidStringView(
                context,
                SolidRenderTheme(solidMapDirectory, FocAndroidFactory(context)),
                theme
            )
        )
        add(SolidIndexListView(context, SolidScaleFactor(storage), theme))
        add(SolidCheckBox(acontext, MapsForge(storage), theme))
        add(TitleView(context, Res.str().p_dem(), theme))
        add(SolidDirectoryViewSAF(acontext, AndroidSolidDem3Directory(context), theme))
        add(SolidCheckBox(acontext, SolidDem3EnableDownload(storage), theme))
        add(TitleView(context, ElevationSource.ELEVATION_HILLSHADE.name, theme))
        add(SolidCheckBox(acontext, HillShade(storage), theme))
        add(TitleView(context, context.getString(R.string.p_trim_cache), theme))
        add(SolidIndexListView(context, SolidTrimMode(storage), theme))
        add(SolidIndexListView(context, SolidTrimSize(storage), theme))
        add(SolidIndexListView(context, SolidTrimDate(storage), theme))
        tileRemover = TileRemoverView(scontext, acontext, theme)
        add(tileRemover)
    }

    fun updateText() {
        tileRemover.updateText()
    }
}
