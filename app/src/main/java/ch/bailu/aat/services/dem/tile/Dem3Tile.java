package ch.bailu.aat.services.dem.tile;

import android.content.Context;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.util.LatLongUtils;

import java.util.zip.ZipInputStream;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.background.FileHandle;
import ch.bailu.aat.services.background.ProcessHandle;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public class Dem3Tile implements ElevationProvider, DemProvider {

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

    public static final DemProvider NULL = new DemProvider() {

        @Override
        public short getElevation(int index) {
            return 0;
        }

        @Override
        public DemDimension getDim() {
            return DIMENSION;
        }

        @Override
        public float getCellsize() {
            return 50;
        }

        @Override
        public boolean inverseLatitude() {
            return false;
        }

        @Override
        public boolean inverseLongitude() {
            return false;
        }

    };

    private static final int DEM3_BUFFER_DIM=1201;
    private static final int DEM3_BUFFER_OFFSET=1;
    private static final DemDimension DIMENSION=
            new DemDimension(DEM3_BUFFER_DIM, DEM3_BUFFER_OFFSET);

    private final byte data[]= new byte[DEM3_BUFFER_DIM*DEM3_BUFFER_DIM*2];

    private ProcessHandle handle=FileHandle.NULL;

    private int lock=0;
    private boolean loading=false;


    private long stamp=System.currentTimeMillis();

    private final DemGeoToIndex toIndex = new DemGeoToIndex(DIMENSION);

    private SrtmCoordinates coordinates = new SrtmCoordinates(0,0);

    public long getTimeStamp() {
        return stamp;
    }


    public synchronized void lock() {
        lock++;
    }


    public synchronized void free() {
        lock--;
    }


    private boolean isLocked() {
        return lock != 0;
    }

    public boolean isProcessed() {

        return (!loading && !isLocked());
    }


    public boolean isLoading() {
        return loading;
    }
    public boolean isLoaded() {
        return !loading;
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }


    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }



    public void load(ServiceContext cs, SrtmCoordinates c) {
        if (!isLocked()) {
            coordinates=c;
            reload(cs);
        }
    }


    public void reload(ServiceContext cs) {
        handle.stopLoading();
        handle = new SRTMGL3Loader(coordinates.toFile(cs.getContext()));
        loading=true;
        stamp=System.currentTimeMillis();
        cs.getBackgroundService().load(handle);
    }

    private class SRTMGL3Loader extends FileHandle {

        public SRTMGL3Loader(Foc f) {
            super(f);
        }

        @Override
        public long bgOnProcess() {
            ZipInputStream zip=null;

            try {
                zip = new ZipInputStream(getFile().openR());



                int total=0;

                zip.getNextEntry();

                int count;

                do {
                    count = zip.read(data, total, data.length-total);
                    total += count;

                } while(count > 0 && total < data.length && canContinue()) ;


            } catch (Exception e) {
                for (int i=0; i<data.length; i++) data[i]=0;
                AppLog.d(getFile(), "ERROR: " + e.toString());
            } finally {
                Foc.close(zip);
            }

            return data.length;
        }

        @Override
        public void broadcast(Context context) {
            loading=false;
            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, toString());

        }
    }


    @Override
    public DemDimension getDim() {
        return DIMENSION;
    }





    @Override
    public short getElevation(int laE6, int loE6) {
        return getElevation(toIndex.toPos(laE6, loE6));
    }

    @Override
    public short getElevation(int index) {
        index = index *2;

        final short x = (short) ((data[index] << 8) | (data[index+1]&0xFF));
        return x;
    }


    private final static double REF_LO_1=7d;
    private final static double REF_LO_2=8d;

    @Override
    public float getCellsize() {


        final float fdistance = (float) LatLongUtils.sphericalDistance(
                new LatLong(coordinates.getLatitudeE6()/1e6, REF_LO_1),
                new LatLong(coordinates.getLatitudeE6()/1e6, REF_LO_2));

        float idistance = fdistance / (DIMENSION.DIM-DIMENSION.OFFSET);

        if (idistance==0) idistance=50;

        return idistance;
    }

    @Override
    public boolean inverseLatitude() {
        return (coordinates.getLatitudeE6()>0);
    }


    @Override
    public boolean inverseLongitude() {
        return (coordinates.getLongitudeE6()<0);
    }

}
