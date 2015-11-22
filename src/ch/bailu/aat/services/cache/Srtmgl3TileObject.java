package ch.bailu.aat.services.cache;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.content.Context;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.background.BackgroundService;
import ch.bailu.aat.services.background.DownloadHandle;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.services.cache.CacheService.SelfOn;

public class Srtmgl3TileObject extends ElevationProviderObject {
    /** 
     * SRTM
     * 
     *  
     * The Shuttle Radar Topography Mission (SRTM) (Wikipedia article) is a NASA mission conducted in 2000 
     * to obtain elevation data for most of the world. It is the current dataset of choice for digital 
     * elevation model data (DEM) since it has a fairly high resolution (1 arc-second, or around 25 meters, 
     * for the United States, and 3 arc-second, or around 90 meters at the equator, for the rest of the world),
     * has near-global coverage (from 56°S to 60°N), and is in the public domain.
     * 
     * Many OpenStreetMap-based projects use SRTM data to provide topography information, relief shading,
     * and elevation profiles for trails and routes. An example is the OpenCycleMap rendering which shows contours
     * and relief shading derived from SRTM data.  
     *
     *
     *
     * Format
     * 
     * The official 3-arc-second and 1-arc-second data for versions 2.1 and 3.0 are divided into 1°×1° data tiles. 
     * The tiles are distributed as zip files containing HGT files labeled with the coordinate of the southwest cell. 
     * For example, the file N20E100.hgt contains data from 20°N to 21°N and from 100°E to 101°E inclusive.
     * 
     * The HGT files have a very simple format. Each file is a series of 16-bit integers giving the height of each cell 
     * in meters arranged from west to east and then north to south. Each 3-arc-second data tile has 144201 integers 
     * representing a 1201×1201 grid, while each 1-arc-second data tile has 12967201 integers representing a 3601×3601 grid. 
     * The outermost rows and columns of each tile overlap with the corresponding rows and columns of adjacent tiles.
     * 
     * Recent versions of GDAL support the HGT files natively (as long as you don't rename the files; the names they
     * come with are the source of their georeferencing), but the srtm_generate_hdr.sh script can also be used to create
     * a GeoTIFF from the HGT zip files. (Note that the script has SRTM3 values hardcoded; if you're using SRTM1, you'll
     * have to change the number of rows and columns to 3601, the number of row bytes to 7202, and the pixel dimensions 
     * to 0.000277777777778.)
     * 
     * 
     * Source: http://wiki.openstreetmap.org/wiki/SRTM
     */



    public static final int SRTM_BUFFER_DIM=1201;
    public static final int SRTM_DIM=SRTM_BUFFER_DIM-1;

    private final byte data[]= new byte[SRTM_BUFFER_DIM*SRTM_BUFFER_DIM*2];
    private final ShortBuffer buffer = ByteBuffer.wrap(data).asShortBuffer();    


    private boolean locked=false;

    private final SRTMGL3Loader load;
    private final String url;
    

   
    
    public Srtmgl3TileObject(String id, SelfOn self, String u) {
        super(id);
        url = u;
        load = new SRTMGL3Loader(id);
    }


    @Override
    public void onInsert(SelfOn self) {
        final File file =new File(toString());
        
        super.onInsert(self);
        
        
        self.broadcaster.put(this);
        
        if (file.exists()==false) {
            self.background.download(new DownloadHandle(url, file));
        } else {
            
            load(self.background);
        }
    }
    
    
    @Override
    public void onRemove(SelfOn self) {
        load.stopLoading();
    }
    
    
    private void load(BackgroundService bg) {
        if (new File(toString()).exists() && load.isLocked()==false) {
            locked=true;
            bg.load(load);
        }
    }

    @Override
    public long getSize() {
        return data.length;
    }

    private class SRTMGL3Loader extends FileHandle {

        public SRTMGL3Loader(String f) {
            super(f);
        }

        @Override
        public long bgOnProcess() {
            InputStream input=null;
            
            try {
                
                
                ZipFile zip= new ZipFile(toString());
                
                if (zip.size()>0) {
                    int total=0;

                    final ZipEntry entry = (ZipEntry) zip.entries().nextElement();
                    input = new BufferedInputStream(zip.getInputStream(entry));
                    
                    int count=0;

                    do {
                        count = input.read(data, total, data.length-total);
                        total += count;

                    } while(count > 0 && total < data.length && canContinue()) ;
                    
                }
                zip.close();


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (input!=null)
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                
            }
            return getSize();
        }

        @Override
        public void broadcast(Context context) {
            locked=false;
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, Srtmgl3TileObject.this.toString());            
        }
    };


    
    @Override
    public short getElevation(int laE6, int loE6) {
        int x=toXPos(loE6);
        int y=toYPos(laE6);

        return buffer.get(y*SRTM_BUFFER_DIM + x);
    }


    @Override
    public short getElevation(int index) {
        return buffer.get(index);
    }
    
    private static int inverse(int v) {
        return SRTM_DIM-1-v;    
    }


    public static int toXPos(int loE6) {
        if (loE6<0) return inverse(toPos(loE6));
        return toPos(loE6);
    }
    

    public static int toYPos(int laE6) {
        if (laE6 >0) return inverse(toPos(laE6));
        return toPos(laE6);
    }
    
    public static int toPos(int cE6) {
        double c = Math.abs(cE6);
        
        c = c / 1e6d;
        
        final double deg = (int)c;

        final double min  =(c-deg);
        final double x = min*60d*20d;
        return (int)x;
    }


    @Override
    public boolean isReady() {
        return locked==false;
    }



    @Override
    public void onDownloaded(String id, String u, SelfOn self) {
        if (u.equals(url)) load(self.background);
    }


    @Override
    public void onChanged(String id, SelfOn self) {}
    
    
    @Override
    public long getAccessTime() {
        return makeOld(super.getAccessTime());
    }
};
