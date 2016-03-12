package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;
/*
public class ElevationShadeTile extends ElevationTile {

    public ElevationShadeTile(String id, SelfOn self, MapTile t) {
        super(id, self, t);
    }

    @Override
    public void fillBitmap(int[] buffer, int[] toLaRaster, int[] toLoRaster,
            Span laSpan, Span loSpan, DemProvider srtm) {
        final int dim = srtm.getDim().DIM_OFFSET;
        final int fact = 5;
        int c=0;
        for (int la=laSpan.start; la< laSpan.end; la++) {
            for (int lo=loSpan.start; lo<loSpan.end; lo++) {
                final short altitude1 = srtm.getElevation(toLaRaster[la] * dim + toLoRaster[lo]);
                final short altitude2 = srtm.getElevation(toLaRaster[la] * dim + toLoRaster[lo]+1);
                
                int gain = (altitude2-altitude1)*fact;

                int x;
                if (gain <0) {
                    gain = Math.abs(gain);
                    x=255;
                } else {
                    x=0;
                }
                
                gain = Math.min(gain, 255);

                final int color = Color.argb(gain, x, x,x);
                
                buffer[c]=color;
                c++;
            }
        }        
    }

    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            return  new ElevationShadeTile(id, self, mapTile);
        }
    }

}
*/