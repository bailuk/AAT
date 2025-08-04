package ch.bailu.aat_lib.service.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.elevation.loader.Dem3Loader
import ch.bailu.aat_lib.service.elevation.loader.Dem3Tiles
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdater
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient

class ElevationService(appContext: AppContext) : VirtualService(),
    ElevationServiceInterface {
    private val updater: ElevationUpdater
    private val loader: Dem3Loader

    init {
        val tiles = Dem3Tiles()
        loader = Dem3Loader(appContext, appContext.createTimer(), tiles)
        updater = ElevationUpdater(appContext, loader, tiles)
    }

    override fun requestElevationUpdates(client: ElevationUpdaterClient, dem3Coordinates: List<Dem3Coordinates>) {
        updater.requestElevationUpdates(client, dem3Coordinates)
    }

    override fun requestElevationUpdates() {
        updater.requestElevationUpdates()
    }


    override fun cancelElevationUpdates(client: ElevationUpdaterClient) {
        updater.cancelElevationUpdates(client)
    }

    override fun getElevation(laE6: Int, loE6: Int): Short {
        return loader.getElevation(laE6, loE6)
    }

    override fun close() {
        updater.close()
        loader.close()
    }
}
