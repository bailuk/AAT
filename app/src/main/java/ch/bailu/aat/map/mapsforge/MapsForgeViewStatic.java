package ch.bailu.aat.map.mapsforge;

import android.view.MotionEvent;

import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.TileProviderInterface;
import ch.bailu.aat.map.tile.TileProviderStatic;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.BitmapTileObject;

public class MapsForgeViewStatic extends MapsForgeViewBase {

    public MapsForgeViewStatic(ServiceContext sc) {
        this(
                sc,
                new TileProvider(sc, BitmapTileObject.MAPNIK),
                new SolidTileSize(sc.getContext()).getTileSize());
    }


    public MapsForgeViewStatic(ServiceContext sc, int tileSize) {
        this(
                sc,
                new TileProviderStatic(sc, BitmapTileObject.MAPNIK),
                BitmapTileObject.TILE_SIZE
        );
    }


    private MapsForgeViewStatic(ServiceContext sc,
                               TileProviderInterface provider,
                               int tileSize) {
        super(sc, MapsForgeViewStatic.class.getSimpleName(), tileSize);

        MapsForgeTileLayer tiles = new MapsForgeTileLayer(
                provider,
                MapsForgeTileLayer.OPAQUE);

        add(tiles, tiles);
        setClickable(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
