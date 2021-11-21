package ch.bailu.aat.map.mapsforge;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.app.AppContext;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    public MapsForgeViewStatic(AppContext appContext, ServiceContext sc) {
        super(sc, MapsForgeViewStatic.class.getSimpleName(),
                new MapDensity(sc.getContext()));

        MapsForgeTileLayerStackConfigured tiles = new MapsForgeTileLayerStackConfigured.BackgroundOnly(this, appContext);
        add(tiles, tiles);

        setClickable(false);
    }

}
