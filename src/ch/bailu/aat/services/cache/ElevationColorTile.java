package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.views.graph.ColorTable;

public class ElevationColorTile extends ElevationTile {

    public ElevationColorTile(String id, SelfOn self, MapTile t, int _split) {
        super(id, self, t, _split);
    }

   

    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster, Span laSpan, Span loSpan, DemProvider dem) {
        final int dim = dem.getDim().DIM_OFFSET;
        final int bitmap_dim = loSpan.size();

        int c=0;
        int old_line=-1;
        
        
        for (int la=laSpan.start; la< laSpan.end; la++) {

            final int line = toLaRaster[la]*dim;
            int offset = toLoRaster[loSpan.start];

            if (old_line != line) {
                shade.setAltitude(dem.getElevation(line + offset));

                for (int lo=loSpan.start; lo<loSpan.end; lo++) {
                    final int new_offset=toLoRaster[lo];

                    if (new_offset != offset) {
                        offset=new_offset;

                        shade.setAltitude(dem.getElevation(line + offset));
                    }


                    buffer[c]=shade.color;
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
    
    private class ShadeColor {
        public int color=0;
        private short altitude;
        
        
        public void setAltitude(short a) {
            altitude=a;
            changeColor();
            
        }

        


        private void changeColor() {
            color = ColorTable.altitude.getColor(altitude);
        }

    } 
    private ShadeColor shade=new ShadeColor();

    public static class Factory extends ObjectHandle.Factory {
        private static final int SPLIT=0;
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        
        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return  new ElevationColorTile(id, self, mapTile,SPLIT);
        }
    }

}
