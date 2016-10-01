package ch.bailu.aat.views.preferences;

import android.content.Context;

import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.SolidTrimDate;
import ch.bailu.aat.preferences.SolidTrimMode;
import ch.bailu.aat.preferences.SolidTrimSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.tileremover.TileRemoverView;

public class MapTilePreferencesView extends VerticalScrollView {
    private final TileRemoverView tileRemover;

    public MapTilePreferencesView(ServiceContext scontext) {
        super(scontext.getContext());

        final Context context = scontext.getContext();

        add(new TitleView(context, "Map Tiles*"));
        add(new SolidIndexListView(context,new SolidTileSize(context)));
        add(new SolidIndexListView(context,new SolidTileCacheDirectory(context)));
        add(new SolidIndexListView(context,new SolidTrimMode(context)));
        add(new SolidIndexListView(context,new SolidTrimSize(context)));
        add(new SolidIndexListView(context,new SolidTrimDate(context)));

        tileRemover = new TileRemoverView(scontext);
        add(tileRemover);
    }

    public void updateText() {
        tileRemover.updateText();
    }
}
