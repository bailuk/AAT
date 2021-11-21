package ch.bailu.aat.views.preferences;

import android.app.Activity;
import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.factory.AndroidFocFactory;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.preferences.map.AndroidSolidDem3Directory;
import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.AndroidSolidTileCacheDirectory;
import ch.bailu.aat.preferences.map.SolidDem3EnableDownload;
import ch.bailu.aat.preferences.map.SolidEnableTileCache;
import ch.bailu.aat.preferences.map.SolidMapsForgeMapFile;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.map.SolidTrimDate;
import ch.bailu.aat.preferences.map.SolidTrimMode;
import ch.bailu.aat.preferences.map.SolidTrimSize;
import ch.bailu.aat.preferences.system.SolidVolumeKeys;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AndroidAppDensity;
import ch.bailu.aat.util.ui.UiTheme;
import ch.bailu.aat.views.tileremover.TileRemoverView;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidTileSize;
import ch.bailu.aat_lib.resources.ToDo;

public class MapTilePreferencesView extends VerticalScrollView {
    private final TileRemoverView tileRemover;


    public MapTilePreferencesView(Activity acontext, ServiceContext scontext, UiTheme theme) {
        super(scontext.getContext());

        final Context context = scontext.getContext();
        final StorageInterface storage = new Storage(context);


        add(new TitleView(context, context.getString(R.string.p_tiles), theme));
        add(new SolidIndexListView(context,new SolidTileSize(storage, new AndroidAppDensity(context)), theme));
        add(new SolidDirectoryViewSAF(acontext, new AndroidSolidTileCacheDirectory(context), theme));
        add(new SolidCheckBox(acontext, new SolidVolumeKeys(new Storage(context)), theme));

        add(new TitleView(context, MapsForgeSource.NAME, theme));
        add(new SolidDirectoryView(context,new SolidMapsForgeMapFile(context), theme));
        add(new SolidDirectoryView(context,new AndroidSolidMapsForgeDirectory(context), theme));
        add(new SolidStringView(context,new SolidRenderTheme(new AndroidSolidMapsForgeDirectory(context), new AndroidFocFactory(context)), theme));
        add(new SolidCheckBox(acontext, new SolidEnableTileCache.MapsForge(storage), theme));

        add(new TitleView(context, ToDo.translate("Dem3 altitude tiles"), theme));
        add(new SolidDirectoryViewSAF(acontext, new AndroidSolidDem3Directory(context), theme));
        add(new SolidCheckBox(acontext, new SolidDem3EnableDownload(storage), theme));

        add(new TitleView(context, Source.ELEVATION_HILLSHADE.getName(), theme));
        add(new SolidCheckBox(acontext, new SolidEnableTileCache.Hillshade(storage), theme));

        add(new TitleView(context, context.getString(R.string.p_trim_cache), theme));
        add(new SolidIndexListView(context,new SolidTrimMode(context), theme));
        add(new SolidIndexListView(context,new SolidTrimSize(context), theme));
        add(new SolidIndexListView(context,new SolidTrimDate(context), theme));

        tileRemover = new TileRemoverView(scontext, acontext, theme);
        add(tileRemover);
    }

    public void updateText() {
        tileRemover.updateText();
    }
}
