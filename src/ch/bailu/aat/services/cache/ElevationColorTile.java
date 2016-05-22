package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.views.graph.ColorTable;

public class ElevationColorTile extends ElevationTile {

    public ElevationColorTile(String id, ServiceContext cs, MapTile t, int _split) {
        super(id, cs, t, _split);
    }

   

    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster, Span laSpan, Span loSpan, DemProvider dem) {
        final int dim = dem.getDim().DIM;
        final int bitmap_dim = loSpan.size();

        int c=0;
        int old_line=-1;
        int color=0;
        
        for (int la=laSpan.start(); la< laSpan.end(); la++) {

            final int line = toLaRaster[la]*dim;
            int offset = -1; 

            if (old_line != line) {

                
                for (int lo=loSpan.start(); lo<loSpan.end(); lo++) {
                    final int new_offset=toLoRaster[lo];

                    if (new_offset != offset) {
                        offset=new_offset;

                        color=ColorTable.altitude.getColor(dem.getElevation(line + offset));
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
    

    public static class Factory extends ObjectHandle.Factory {
        private static final int SPLIT=0;
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        
        @Override
        public ObjectHandle factory(String id, ServiceContext cs) {
            return  new ElevationColorTile(id, cs, mapTile,SPLIT);
        }
    }

}
