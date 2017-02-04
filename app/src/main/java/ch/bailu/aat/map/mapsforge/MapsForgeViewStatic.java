package ch.bailu.aat.map.mapsforge;

import ch.bailu.aat.map.MapDensity;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    private final static Source SOURCE = DownloadSource.MAPNIK;

    public MapsForgeViewStatic(ServiceContext sc) {
        super(sc, MapsForgeViewStatic.class.getSimpleName(),
                new MapDensity(sc.getContext()));

        MapsForgeTileLayer tiles = new MapsForgeTileLayer(
                sc,
                new TileProvider(sc, SOURCE));

        add(tiles, tiles);

        setClickable(false);

        setZoomLevelMin((byte)SOURCE.getMinimumZoomLevel());
        setZoomLevelMax((byte)SOURCE.getMaximumZoomLevel());
    }

}
