package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.map.SolidEnableTileCache;
import ch.bailu.aat.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.map.SolidRendererThreads;
import ch.bailu.aat.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.map.SolidTileSize;
import ch.bailu.aat.preferences.map.SolidTrimDate;
import ch.bailu.aat.preferences.map.SolidTrimMode;
import ch.bailu.aat.preferences.map.SolidTrimSize;
import ch.bailu.aat.preferences.system.SolidVolumeKeys;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.tileremover.TileRemoverView;

public class MapTilePreferencesView extends VerticalScrollView {
    private final TileRemoverView tileRemover;


    public MapTilePreferencesView(Activity acontext, ServiceContext scontext) {
        super(scontext.getContext());

        final Context context = scontext.getContext();


        add(new TitleView(context, context.getString(R.string.p_tiles)));
        add(new SolidIndexListView(new SolidTileSize(context)));
        add(new SolidDirectoryViewSAF(acontext, new SolidTileCacheDirectory(context)));
        add(new SolidCheckBox(new SolidVolumeKeys(acontext)));

        add(new TitleView(context, MapsForgeSource.NAME));
        add(new SolidDirectoryView(new SolidMapsForgeDirectory(context)));
        add(new SolidStringView(new SolidRenderTheme(context)));
        add(new SolidIndexListView( new SolidRendererThreads(context)));
        add(new SolidCheckBox(new SolidEnableTileCache.MapsForge(context)));

        add(new TitleView(context, Source.ELEVATION_HILLSHADE.getName()));
        add(new SolidCheckBox(new SolidEnableTileCache.Hillshade(context)));

        add(new TitleView(context, context.getString(R.string.p_trim_cache)));
        add(new SolidIndexListView(new SolidTrimMode(context)));
        add(new SolidIndexListView(new SolidTrimSize(context)));
        add(new SolidIndexListView(new SolidTrimDate(context)));

        tileRemover = new TileRemoverView(scontext, acontext);
        add(tileRemover);
    }

    public void updateText() {
        tileRemover.updateText();
    }
}
