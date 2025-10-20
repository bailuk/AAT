package ch.bailu.aat_lib.service.elevation.tile

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.preferences.map.SolidDem3Directory
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.elevation.Dem3Lock
import ch.bailu.aat_lib.service.elevation.Dem3Status
import ch.bailu.aat_lib.service.elevation.ElevationProvider

class Dem3Tile : ElevationProvider, DemProvider {
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
    private var coordinates = Dem3Coordinates(0,0)
    private val status = Dem3Status()
    private val array = Dem3Array()
    private val lock = Dem3Lock()

    private var loader = BackgroundTask.NULL

    override fun toString(): String {
        return coordinates.toString()
    }


    override fun hashCode(): Int {
        return coordinates.hashCode()
    }

    fun reload(sc: ServicesInterface, solidDem3Directory: SolidDem3Directory) {
        load(sc, coordinates, solidDem3Directory)
    }

    @Synchronized
    fun load(service: ServicesInterface, newCoordinates: Dem3Coordinates, solidDem3Directory: SolidDem3Directory) {
        if (!lock.isLocked) {
            loader.stopProcessing()

            synchronized(array) {
                status.status = Dem3Status.LOADING
                coordinates = newCoordinates

                val file = solidDem3Directory.toFile(coordinates)
                loader = Dem3LoaderTask(file, array, status)
                service.getBackgroundService().process(loader)
            }
        }
    }

    fun getCoordinates(): Dem3Coordinates {
        return coordinates
    }

    @get:Synchronized
    val isLocked: Boolean
        get() = lock.isLocked

    @Synchronized
    fun lock() {
        lock.lock()
    }

    @Synchronized
    fun free() {
        lock.free()
    }

    fun getStatus(): Int {
        return status.status
    }


    override fun getElevation(index: Int): Short {
        return array.getElevation(index)
    }

    override fun getDimension(): DemDimension {
        return Dem3Array.dem3Dimension
    }

    override fun getCellDistance(): Float {
        return coordinates.getCellDistance()
    }

    override fun hasInverseLatitude(): Boolean {
        return coordinates.hasInverseLatitude()
    }

    override fun hasInverseLongitude(): Boolean {
        return coordinates.hasInverseLongitude()
    }

    override fun getElevation(laE6: Int, loE6: Int): Short {
        return array.getElevation(laE6, loE6)
    }

    val timeStamp: Long
        get() = status.stamp
}
