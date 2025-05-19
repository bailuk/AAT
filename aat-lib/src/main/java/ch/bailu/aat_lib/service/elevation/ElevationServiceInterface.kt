package ch.bailu.aat_lib.service.elevation

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient

interface ElevationServiceInterface {
    fun getElevation(latitudeE6: Int, longitudeE6: Int): Short
    fun requestElevationUpdates(owner: ElevationUpdaterClient, srtmTileCoordinates: List<Dem3Coordinates>)
    fun requestElevationUpdates()
    fun cancelElevationUpdates(objGpxStatic: ElevationUpdaterClient)
}
