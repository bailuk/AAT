package ch.bailu.aat_lib.service.elevation.tile;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.elevation.Dem3Lock;
import ch.bailu.aat_lib.service.elevation.Dem3Status;
import ch.bailu.aat_lib.service.elevation.ElevationProvider;
import ch.bailu.foc.Foc;

public final class Dem3Tile implements ElevationProvider, DemProvider {

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
     * have to changed the number of rows and columns to 3601, the number of row bytes to 7202, and the pixel dimensions
     * to 0.000277777777778.)
     *
     *
     * Source: http://wiki.openstreetmap.org/wiki/SRTM
     */


    private final ch.bailu.aat_lib.service.elevation.tile.Dem3Coordinates coordinates = new ch.bailu.aat_lib.service.elevation.tile.Dem3Coordinates();
    private final Dem3Status status = new Dem3Status();
    private final Dem3Array array = new Dem3Array();
    private final Dem3Lock lock = new Dem3Lock();


    private BackgroundTask loader = BackgroundTask.NULL;

    @Nonnull
    @Override
    public String toString() {
        return coordinates.coordinates.toString();
    }


    @Override
    public int hashCode() {
        return coordinates.coordinates.hashCode();
    }

    public void reload(ServicesInterface sc, SolidDem3Directory solidDem3Directory) {
        load(sc, coordinates.coordinates, solidDem3Directory);
    }

    public  synchronized void load(ServicesInterface sc, Dem3Coordinates c, SolidDem3Directory solidDem3Directory) {
        if (!lock.isLocked()) {
            loader.stopProcessing();

            synchronized (array) {
                status.setStatus(Dem3Status.LOADING);
                coordinates.coordinates = c;

                Foc file = solidDem3Directory.toFile(c);
                loader = new Dem3LoaderTask(file, array, status);

                sc.getBackgroundService().process(loader);
            }
        }
    }

    public Dem3Coordinates getCoordinates() {
        return coordinates.coordinates;
    }

    public synchronized boolean isLocked() {
        return lock.isLocked();
    }

    public synchronized void lock(Object o) {
        lock.lock(o);
    }

    public synchronized void free(Object o) {
        lock.free(o);
    }

    public int getStatus() {
        return status.getStatus();
    }


    @Override
    public short getElevation(int index) {
        return array.getElevation(index);
    }

    @Override
    public DemDimension getDim() {
        return array.getDim();
    }

    @Override
    public float getCellsize() {
        return coordinates.getCellsize();
    }

    @Override
    public boolean inverseLatitude() {
        return coordinates.inverseLatitude();
    }

    @Override
    public boolean inverseLongitude() {
        return coordinates.inverseLongitude();
    }

    @Override
    public short getElevation(int laE6, int loE6) {
        return array.getElevation(laE6, loE6);
    }

    public long getTimeStamp() {
        return status.getStamp();
    }
}
