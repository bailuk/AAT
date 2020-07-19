package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.map.SolidEnableTileCache;
import ch.bailu.aat.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidMapsForgeMapFile;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.map.SolidRendererThreads;
import ch.bailu.aat.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.preferences.map.SolidTrimDate;
import ch.bailu.aat.preferences.map.SolidTrimMode;
import ch.bailu.aat.preferences.map.SolidTrimSize;
import ch.bailu.aat.preferences.system.SolidVolumeKeys;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.tileremover.TileRemoverView;

public class MapTilePreferencesView extends VerticalScrollView {
    private final TileRemoverView tileRemover;


    public MapTilePreferencesView(Activity acontext, ServiceContext scontext, UiTheme theme) {
        super(scontext.getContext());

        final Context context = scontext.getContext();


        add(new TitleView(context, context.getString(R.string.p_tiles), theme));
        add(new SolidIndexListView(new SolidTileSize(context), theme));
        add(new SolidDirectoryViewSAF(acontext, new SolidTileCacheDirectory(context), theme));
        add(new SolidCheckBox(new SolidVolumeKeys(acontext), theme));

        add(new TitleView(context, MapsForgeSource.NAME, theme));
        add(new SolidDirectoryView(new SolidMapsForgeMapFile(context), theme));
        add(new SolidDirectoryView(new SolidMapsForgeDirectory(context), theme));
        add(new SolidStringView(new SolidRenderTheme(context), theme));
        add(new SolidIndexListView( new SolidRendererThreads(context), theme));
        add(new SolidCheckBox(new SolidEnableTileCache.MapsForge(context), theme));

        add(new TitleView(context, Source.ELEVATION_HILLSHADE.getName(), theme));
        add(new SolidCheckBox(new SolidEnableTileCache.Hillshade(context), theme));

        add(new TitleView(context, context.getString(R.string.p_trim_cache), theme));
        add(new SolidIndexListView(new SolidTrimMode(context), theme));
        add(new SolidIndexListView(new SolidTrimSize(context), theme));
        add(new SolidIndexListView(new SolidTrimDate(context), theme));

        tileRemover = new TileRemoverView(scontext, acontext, theme);
        add(tileRemover);
    }

    public void updateText() {
        tileRemover.updateText();
    }
}
