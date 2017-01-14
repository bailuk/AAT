package ch.bailu.aat.map.mapsforge;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.BitmapTileObject;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    private final static BitmapTileObject.Source SOURCE = BitmapTileObject.MAPNIK;

    public MapsForgeViewStatic(ServiceContext sc) {
        super(sc, MapsForgeViewStatic.class.getSimpleName(),
                new MapDensity(sc.getContext()));

        MapsForgeTileLayer tiles = new MapsForgeTileLayer(
                new TileProvider(sc, SOURCE),
                MapsForgeTileLayer.OPAQUE);

        add(tiles, tiles);

        setClickable(false);

        setZoomLevelMin((byte)SOURCE.getMinimumZoomLevel());
        setZoomLevelMax((byte)SOURCE.getMaximumZoomLevel());
    }

}
