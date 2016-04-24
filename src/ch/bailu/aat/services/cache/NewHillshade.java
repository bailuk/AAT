package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;
import ch.bailu.aat.services.dem.MultiCell8;

public class NewHillshade extends ElevationTile {

    public NewHillshade(String id, SelfOn self, MapTile t) {
        super(id, self, t, ElevationTile.splitFromZoom(t.getZoomLevel()));
    }

    
    @Override
    public void fillBitmap(int[] bitmap, int[] toLaRaster, int[] toLoRaster,
            Span laSpan, Span loSpan, DemProvider demtile) {
        final int demtile_dim = demtile.getDim().DIM_OFFSET;
        final int bitmap_dim = loSpan.size();

        int color=0;
        int index=0;
        int old_line=-1;

        final MultiCell8 mcell=new MultiCell8(demtile);
        final Hillshade shade=new Hillshade();
        
        for (int la=laSpan.start(); la< laSpan.end(); la++) {

            final int line = toLaRaster[la]*demtile_dim;

            if (old_line != line) {
                int old_offset = -1;
                
                for (int lo=loSpan.start(); lo<loSpan.end(); lo++) {
                    final int offset=toLoRaster[lo];

                    if (old_offset != offset) {
                        old_offset = offset;

                        mcell.set(line+offset);
                        color = shade.hillshade(mcell);
                    }
                    
                    bitmap[index]=color;
                    index++;
                }
            } else {
                copyLine(bitmap, index-bitmap_dim, index);
                index+=bitmap_dim;
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
    
    
    private static class Hillshade {
        /**
         * Source: 
         * http://edndoc.esri.com/arcobjects/9.2/net/shared/geoprocessing/spatial_analyst_tools/how_hillshade_works.htm
         */
        
        private static final int    COLOR=50;
        private static final double ALTITUDE_DEG=45d;

        private static final double AZIMUTH_DEG=315d;
        private static final double AZIMUTH_MATH = 360d - AZIMUTH_DEG + 90d;
        private        final double AZIMUTH_RAD = Math.toRadians(AZIMUTH_MATH);
        
        private static final double ZENITH_DEG=90d-ALTITUDE_DEG;
        private        final double ZENITH_RAD=Math.toRadians(ZENITH_DEG);
        private        final double ZENITH_COS=Math.cos(ZENITH_RAD);
        private        final double ZENITH_SIN=Math.sin(ZENITH_RAD);
        

        private final static double DOUBLE_PI=Math.PI*2d;
        private final static double HALF_PI=Math.PI/2d;
        private final static double ONEHALF_PI=DOUBLE_PI-HALF_PI;

        public int hillshade(final MultiCell8 mcell) 
        {
            final double dzx = mcell.delta_zx();
            final double dzy = mcell.delta_zy();
            final double slope=slope_rad(dzx, dzy);
            
            int shade = (int) (255d * (( ZENITH_COS * Math.cos(slope) ) + 
                    ( ZENITH_SIN * Math.sin(slope) * Math.cos(AZIMUTH_RAD - aspect_rad(dzx, dzy)) ) ));
            
            shade = Math.max(0, shade); 
                    
            return alphaColor(shade);
        }
        
        private int alphaColor(final int shade) {
            return Color.argb(255-shade, COLOR,COLOR,COLOR);
        }
        
 
    
        
        
        private double slope_rad(final double dzx, final double dzy) {
            return Math.atan(Math.sqrt(dzx*dzx + dzy*dzy));
        }
        
        
        
        private double aspect_rad(final double dzx, final double dzy) {
            double ret=0d;
            
            if (dzx!=0d) {

                ret = Math.atan2(dzy, -1d*dzx);

                if (ret < 0d) {

                  ret = DOUBLE_PI + ret;
                }
                
            } else {

                if (dzy > 0d) {
                    ret = HALF_PI;

                } else if (dzy < 0d) {
                  ret = ONEHALF_PI;
                } 
            }
            return ret;
        }
     }

    public static class Factory extends ObjectHandle.Factory {
        private final MapTile mapTile;

        public Factory(MapTile t) {
            mapTile=t;
        }

        @Override
        public ObjectHandle factory(String id, SelfOn self) {
            
            return  new NewHillshade(id, self, mapTile);
        }
        
    } 
}
