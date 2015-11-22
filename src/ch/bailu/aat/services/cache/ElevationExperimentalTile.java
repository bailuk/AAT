package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.srtm.SrtmAccess;

public class ElevationExperimentalTile extends ElevationTile{

    public ElevationExperimentalTile(String id, SelfOn self, MapTile t) {
        super(id, self, t);
    }

    

    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster,
            Span laSpan, Span loSpan, SrtmAccess srtm) {
        int c=0;
        for (int la=laSpan.start; la< laSpan.end; la++) {
            for (int lo=loSpan.start; lo<loSpan.end; lo++) {
                final int altitude1 = srtm.getElevation(toLaRaster[la] * Srtmgl3TileObject.SRTM_BUFFER_DIM + toLoRaster[lo]);
                final int altitude2 = srtm.getElevation(toLaRaster[la] * Srtmgl3TileObject.SRTM_BUFFER_DIM + toLoRaster[lo]+1);
                
                int gain = (altitude2-altitude1)*10;
                
                gain = Math.max(gain, 0);
                gain = Math.min(gain, 255);
                final int igain = 255-gain;

                final int color = Color.rgb(igain, igain,igain);

                
                
                
                
                buffer[c]=color;
                c++;
            }
        }                
    }

    
    private static void drawMegaPixel(int w, int h, int pixelColor, int leftColor, int bottomColor, int cornerColor) {
        // 1. calculate steps (left bottom corner)
        // 
        
    }
    
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
