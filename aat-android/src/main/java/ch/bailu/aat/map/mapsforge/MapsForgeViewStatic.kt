package ch.bailu.aat.map.mapsforge;

import android.content.Context;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat_lib.map.tile.MapsForgeTileLayerStackConfigured;
import ch.bailu.aat_lib.app.AppContext;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    public MapsForgeViewStatic(Context context, AppContext appContext) {
        super(appContext, context, MapsForgeViewStatic.class.getSimpleName(),
                new MapDensity(context));

        MapsForgeTileLayerStackConfigured tiles = new MapsForgeTileLayerStackConfigured.BackgroundOnly(this, appContext);
        add(tiles, tiles);

        setClickable(false);
    }
}
