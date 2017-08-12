package ch.bailu.aat.map.mapsforge;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    public MapsForgeViewStatic(ServiceContext sc) {
        super(sc, MapsForgeViewStatic.class.getSimpleName(),
                new MapDensity(sc.getContext()));

        MapsForgeTileLayerStackConfigured tiles = new MapsForgeTileLayerStackConfigured.BackgroundOnly(this);
        add(tiles, tiles);

        setClickable(false);
    }

}
