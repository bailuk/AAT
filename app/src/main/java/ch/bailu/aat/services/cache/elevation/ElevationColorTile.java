package ch.bailu.aat.services.cache.elevation;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.services.dem.tile.DemProvider;
import ch.bailu.aat.views.graph.AltitudeColorTable;

public class ElevationColorTile extends ElevationTile {


    public ElevationColorTile(String id, Tile t, int _split) {
        super(id,  t, _split);
    }

   

    @Override
    public void fillBuffer(int[] buffer, Raster raster, SubTile subTile, DemProvider dem) {
        final int dim = dem.getDim().DIM;
        final int bitmap_dim = subTile.pixelDim();

        int c=0;
        int old_line=-1;
        int color=0;
        
        for (int la = subTile.laSpan.firstPixelIndex(); la< subTile.laSpan.lastPixelIndex(); la++) {

            final int line = raster.toLaRaster[la]*dim;
            int offset = -1; 

            if (old_line != line) {

                
                for (int lo = subTile.loSpan.firstPixelIndex(); lo<subTile.loSpan.lastPixelIndex(); lo++) {
                    final int new_offset=raster.toLoRaster[lo];

                    if (new_offset != offset) {
                        offset=new_offset;

                        color= AltitudeColorTable.INSTANCE.getColor(dem.getElevation(line + offset));
                    }

                    buffer[c]=color;
                    c++;
                }
            } else {
                copyLine(buffer, c-bitmap_dim, c);
                c+=bitmap_dim;
            }
            
            old_line=line;
        }                
    }
    
    
    private void copyLine(int[] buffer, int cs, int cd) {
        final int next_line=cd;
        
        for (; cs < next_line; cs++) {
            buffer[cd]=buffer[cs];
            cd++;
        }
    }

    @Override
    public long getSize() {
        return TileObject.getBytesHack(TileObject.TILE_SIZE);
    }



    public static class Factory extends ObjectHandle.Factory {
        private static final int SPLIT=0;
        private final Tile mapTile;

        public Factory(Tile t) {
            mapTile=t;
        }

        
        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return  new ElevationColorTile(id, mapTile,SPLIT);
        }
    }




}
