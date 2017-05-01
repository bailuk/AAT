package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.tile.source.CachedSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.SolidEnableTileCache;
import ch.bailu.aat.preferences.SolidMapsForgeDirectory;
import ch.bailu.aat.preferences.SolidRenderTheme;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.SolidTrimDate;
import ch.bailu.aat.preferences.SolidTrimMode;
import ch.bailu.aat.preferences.SolidTrimSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.tileremover.TileRemoverView;

public class MapTilePreferencesView extends VerticalScrollView {
    private final TileRemoverView tileRemover;


    public MapTilePreferencesView(Activity acontext, ServiceContext scontext) {
        super(scontext.getContext());

        final Context context = scontext.getContext();

        add(new TitleView(context, context.getString(R.string.p_tiles)));
        add(new SolidIndexListView(new SolidTileSize(context)));
        add(new SolidExtendetDirectoryView(acontext, new SolidTileCacheDirectory(context)));

        add(new TitleView(context, Source.MAPSFORGE.getName()));
        add(new SolidExtendetDirectoryView(acontext, new SolidMapsForgeDirectory(context)));
        add(new SolidStringView(new SolidRenderTheme(context)));
        add(new SolidEnableTileCacheView(
                acontext,
                new SolidEnableTileCache(context, CachedSource.CACHED_MAPSFORGE)));

        add(new TitleView(context, Source.ELEVATION_HILLSHADE.getName()));
        add(new SolidEnableTileCacheView(
                acontext,
                new SolidEnableTileCache(context, CachedSource.CACHED_ELEVATION_HILLSHADE)));

        add(new TitleView(context, "Trim tile caches*"));
        add(new SolidIndexListView(new SolidTrimMode(context)));
        add(new SolidIndexListView(new SolidTrimSize(context)));
        add(new SolidIndexListView(new SolidTrimDate(context)));

        tileRemover = new TileRemoverView(scontext);
        add(tileRemover);
    }

    public void updateText() {
        tileRemover.updateText();
    }
}
