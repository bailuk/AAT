package ch.bailu.aat_lib.service.elevation

import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient

interface ElevationServiceInterface: ElevationProvider {
    fun requestElevationUpdates(client: ElevationUpdaterClient, dem3Coordinates: List<Dem3Coordinates>)
    fun requestElevationUpdates()
    fun cancelElevationUpdates(client: ElevationUpdaterClient)
}
