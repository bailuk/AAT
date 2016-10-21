package ch.bailu.aat.views.map;

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


    public AbsOsmTileProvider(ServiceContext sc) {
        scontext = sc;
    }

    public TileStackObject getTileHandle(MapTile mapTile) {
        String id = generateTileID(mapTile);
        ObjectHandle handle = scontext.getCacheService().getObject(
                id, 
                new TileStackObject.Factory(scontext.getContext(), mapTile, sources)
                );


        if (TileStackObject.class.isInstance(handle)) {
            return (TileStackObject) handle;

        } else  {
            return TileStackObject.NULL;

        }
    }



    private String generateTileID(MapTile mapTile) {
        builder.setLength(0);

        builder.append(mapTile.getZoomLevel());
        builder.append('/');
        builder.append(mapTile.getX());
        builder.append('/');
        builder.append(mapTile.getY());

        for (Source source : sources) {
            builder.append('/');
            builder.append(source.getName() + source.getBitmapFilter().toString());
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
