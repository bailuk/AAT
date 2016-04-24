package ch.bailu.aat.views.map;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.ITileSource;

import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.TileObject.Source;
import ch.bailu.aat.services.cache.TileStackObject;

public abstract class AbsOsmTileProvider extends AbsTileProvider {
    
    
    private CacheService loader=null;
    
    private Source sources[] = new Source[]{SolidMapTileStack.MAPNIK};
    
    
    private final StringBuilder builder = new StringBuilder();    
    
    public AbsOsmTileProvider(CacheService l) {
        loader=l;
    }

    
    public AbsOsmTileProvider() {}


   
    public TileStackObject getTileHandle(MapTile mapTile) {
        if (loader != null) {
            final String id = generateTileID(mapTile);
            return (TileStackObject) loader.getObject(id, new TileStackObject.Factory(loader, mapTile, sources));
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

        for (int i=0; i<sources.length; i++) {
            builder.append('/');
            builder.append(sources[i].getName()+sources[i].getBitmapFilter().toString());
        }
        return builder.toString();
    }



    @Override
    public ITileSource getTileSource() {
        return null;
    }



    public int getMinimumZoomLevel() {
        return SolidMapTileStack.MIN_ZOOM;
    }


    public int getMaximumZoomLevel() {
        return SolidMapTileStack.MAX_ZOOM;
    }

    
    
    public void setFileLoader(CacheService l) {
        loader = l;
    }

    public void setSubTileSource(Source[] s) {
        sources=s;
    }
    
    @Override
    public void setTileSource(ITileSource s) {};
    

}
