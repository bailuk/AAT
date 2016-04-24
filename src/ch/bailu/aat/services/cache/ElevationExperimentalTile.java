package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;

public class ElevationExperimentalTile extends ElevationTile{
    
    public ElevationExperimentalTile(String id, SelfOn self, MapTile t, float weight, int shift) {
        super(id, self, t, ElevationTile.splitFromZoom(t.getZoomLevel()));
        shade.weight=weight;
        shade.shift=shift;
    }


    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster,
            Span laSpan, Span loSpan, DemProvider tile) {
        final int dim = tile.getDim().DIM_OFFSET;
        final int bitmap_dim = loSpan.size();

        int c=0;
        int old_line=-1;

        int a1_offset, a2_offset;
        if (loSpan.deg() < 0) {
            a1_offset = -1;
            a2_offset = 0;
        } else {
            a1_offset = 0;
            a2_offset = 1;
        }
        
        
        for (int la=laSpan.start(); la< laSpan.end(); la++) {

            final int line = toLaRaster[la]*dim;
            int offset = toLoRaster[loSpan.start()];

            if (old_line != line) {
                shade.setAltitude1(tile.getElevation(line + offset + a1_offset));
                shade.setAltitude2(tile.getElevation(line + offset + a2_offset));

                for (int lo=loSpan.start(); lo<loSpan.end(); lo++) {
                    final int new_offset=toLoRaster[lo];

                    if (new_offset != offset) {
                        offset=new_offset;

                        shade.setAltitude2(tile.getElevation(line + offset + a2_offset));
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
        private final int COLOR=0;
        private float weight=200/90;
        private int shift=30;
        
        public int color=0;
        private int altitude1, altitude2;
        
        public void setAltitude1(short a) {
            altitude1=a;
        }
        
        public void setAltitude2(short a) {
            altitude2=a;
            changeColor();
            altitude1=altitude2;
        }

        

        private void changeColor() {
            int alpha = (int) ((altitude1-altitude2)*weight);
            alpha+=shift;
            
            

            alpha = Math.max(alpha, 0);
            alpha = Math.min(alpha, 255);

            color = Color.argb(alpha, COLOR,COLOR,COLOR);
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
            float weight=2f;
            int  shift=30;
            
            
            switch (mapTile.getZoomLevel()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                weight=0.5f;
                shift=15;
                break;
            case 10:
            case 11:
                weight=2f;
                break;
            case 12: 
                weight=4f;
                break;                
            case 13:
                weight=8f;
                break;
            case 14:
            case 15:
            case 16:
            case 17:
                weight=20f;
                break;
            }
            return  new ElevationExperimentalTile(id, self, mapTile,weight, shift);
        }
        
    } 
}
