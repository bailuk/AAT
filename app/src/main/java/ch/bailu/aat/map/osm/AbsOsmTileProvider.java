package ch.bailu.aat.map.osm;

import org.mapsforge.core.model.Tile;
import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.BitmapTileObject;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject.Source;
import ch.bailu.aat.services.cache.TileStackObject;

public abstract class AbsOsmTileProvider extends AbsTileProvider {

    private final ServiceContext scontext;
    private Source sources[] = new Source[]{BitmapTileObject.MAPNIK};
    private final StringBuilder builder = new StringBuilder();

    private int tileSize = BitmapTileObject.TILE_SIZE;


    public AbsOsmTileProvider(ServiceContext sc) {
        scontext = sc;
    }


    public void setTileSize(int size) {
        tileSize = size;
    }


    public TileStackObject getTileHandle(MapTile mapTile) {
        return getTileHandle(new Tile(mapTile.getX(), mapTile.getY(), (byte)mapTile.getZoomLevel(), tileSize));
    }


    private TileStackObject getTileHandle(Tile mapTile) {
        if (scontext.isUp()) {
            String id = generateTileID(mapTile);
            ObjectHandle handle = scontext.getCacheService().getObject(
                    id,
                    new TileStackObject.Factory(scontext.getContext(), mapTile, sources)
            );


            if (TileStackObject.class.isInstance(handle)) {
                return (TileStackObject) handle;
            }
        }

        return TileStackObject.NULL;
    }



    private String generateTileID(Tile mapTile) {
        builder.setLength(0);

        builder.append(mapTile.zoomLevel);
        builder.append('/');
        builder.append(mapTile.tileX);
        builder.append('/');
        builder.append(mapTile.tileY);

        for (Source source : sources) {
            builder.append('/');
            builder.append(source.getName());
            builder.append(source.getBitmapFilter().toString());
        }
        return builder.toString();
    }


    public int getMinimumZoomLevel() {
        return BitmapTileObject.MIN_ZOOM;
    }
    public int getMaximumZoomLevel() {
        return BitmapTileObject.MAX_ZOOM;
    }

    public void setSubTileSource(Source[] s) {
        sources=s;
    }
}
