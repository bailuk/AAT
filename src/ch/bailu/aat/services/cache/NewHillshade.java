package ch.bailu.aat.services.cache;

import org.osmdroid.tileprovider.MapTile;

import android.graphics.Color;
import ch.bailu.aat.services.cache.CacheService.SelfOn;
import ch.bailu.aat.services.dem.DemProvider;

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

        Hillshade shade=new Hillshade(demtile.getCellsize());
        
        for (int la=laSpan.start(); la< laSpan.end(); la++) {

            final int line = toLaRaster[la]*demtile_dim;

            if (old_line != line) {
                int old_offset = -1;
                
                for (int lo=loSpan.start(); lo<loSpan.end(); lo++) {
                    final int offset=toLoRaster[lo];

                    if (old_offset != offset) {
                        old_offset = offset;

                        final int e=line+offset;
                        
                        final int f=e+1;
                        final int h=e+demtile_dim;
                        final int i=h+1;
                        final int g=h-1;
                        
                        final int d=e-1;
                        final int b=e-demtile_dim;
                        final int c=b+1;
                        final int a=b-1;
                        
                        color = shade.hillshade(
                                demtile.getElevation(a),
                                demtile.getElevation(b),
                                demtile.getElevation(c),
                                demtile.getElevation(d),
                                demtile.getElevation(f),
                                demtile.getElevation(g),
                                demtile.getElevation(h),
                                demtile.getElevation(i));
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
        
        private static final int    COLOR=0;
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

        private final double total_cellsize;
        
        public Hillshade(int cellsize) {
            total_cellsize=cellsize*8;
        }
        
        public int hillshade(
                final short a, 
                final short b, 
                final short c, 
                final short d, 
                final short f, 
                final short g, 
                final short h, 
                final short i) 
        {
            final double dzx = delta_zx(a, c, d, f, g, i);
            final double dzy = delta_zy(a, b, c, g, h, i);
            final double slope=slope_rad(dzx, dzy);
            
            int shade = (int) (255d * (( ZENITH_COS * Math.cos(slope) ) + 
                    ( ZENITH_SIN * Math.sin(slope) * Math.cos(AZIMUTH_RAD - aspect_rad(dzx, dzy)) ) ));
            
            shade = Math.max(0, shade); 
                    
            return alphaColor(shade);
        }
        
        private int alphaColor(final int shade) {
            return Color.argb(255-shade, COLOR,COLOR,COLOR);
        }
        
 /*       
        private int shadeColor(int shade) {
            return Color.argb(255-shade, shade, shade, shade);
        }
        
        private int hyperColor(int shade) {
            final int color=shade;
            int alpha;
            
            if (shade < 128) {
                alpha=255 - shade*2;
            } else { 
                alpha=((shade-128)*2);
            }
            
            // 0== transparent 
            return Color.argb(alpha, color, color, color);
        }
        */
        
        
        private double delta_zx(final short a, final short c, final short d, final short f, final short g, final short i) {
            final double sum = (c + 2*f + i) - (a + 2*d + g); 
            return  (sum) / (total_cellsize);
        }
        
        private double delta_zy(final int a, final int b, final int c, final int g, final int h, final int i) {
            final double sum = (g + 2*h + i) - (a + 2*b + c); 
            return (sum)  / (total_cellsize);
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
