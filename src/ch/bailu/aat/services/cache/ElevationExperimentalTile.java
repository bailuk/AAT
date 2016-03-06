package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.views.graph.ColorTable;

public class ElevationExperimentalTile extends ElevationTile{
    
    public ElevationExperimentalTile(String id, SelfOn self, MapTile t) {
        super(id, self, t);
    }


    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster,
            Span laSpan, Span loSpan, DemProvider tile) {
        final int dim = tile.getDim().DIM_OFFSET;
        final int bitmap_dim = loSpan.size();
        
        shade.meterPerPixel = tile.getDim().METER_PER_PIXEL;
        
        
        int c=0;
        int old_line=-1;
        
        for (int la=laSpan.start; la< laSpan.end; la++) {

            final int line = toLaRaster[la]*dim;
            int offset = toLoRaster[loSpan.start];

            if (old_line != line) {
                shade.setAltitude1(tile.getElevation(line + offset));
                shade.setAltitude2(tile.getElevation(line + offset + 1));

                for (int lo=loSpan.start; lo<loSpan.end; lo++) {
                    final int new_offset=toLoRaster[lo];

                    if (new_offset != offset) {
                        offset=new_offset;

                        shade.setAltitude2(tile.getElevation(line + offset + 1));
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
        private int altitude1, altitude2;
        public int meterPerPixel=90;
        
        public void setAltitude1(short a) {
            altitude1=a;
        }
        
        public void setAltitude2(short a) {
            altitude2=a;
            changeColorHillShade();
            altitude1=altitude2;
        }

        

        private void changeColorHillShade() {
            int gain = ((altitude1-altitude2)*200) / meterPerPixel;
            gain+=30;
            
            final int x=0;

            gain = Math.max(gain, 0);
            gain = Math.min(gain, 255);

            color = Color.argb(gain, x,x,x);
        }

        private void changeColorColorTable() {
            color = ColorTable.altitude.getColor(altitude1);
        }

    } 
    private ShadeColor shade=new ShadeColor();
    
    
    
    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return  new ElevationExperimentalTile(id, self, mapTile);
        }
    } 
}
