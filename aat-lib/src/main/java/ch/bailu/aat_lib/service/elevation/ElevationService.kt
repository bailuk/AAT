package ch.bailu.aat_lib.service.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.coordinates.Dem3Coordinates
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.service.elevation.loader.Dem3Loader
import ch.bailu.aat_lib.service.elevation.loader.Dem3Tiles
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdater
import ch.bailu.aat_lib.service.elevation.updater.ElevationUpdaterClient

class ElevationService(appContext: AppContext) : VirtualService(),
    ElevationProvider, ElevationServiceInterface {
    private val updater: ElevationUpdater
    private val loader: Dem3Loader

    init {
        val tiles = Dem3Tiles()
        loader = Dem3Loader(appContext, appContext.createTimer(), tiles)
        updater = ElevationUpdater(appContext, loader, tiles)
    }

    override fun requestElevationUpdates(e: ElevationUpdaterClient, c: List<Dem3Coordinates>) {
        updater.requestElevationUpdates(e, c)
    }

    override fun requestElevationUpdates() {
        updater.requestElevationUpdates()
    }


    override fun cancelElevationUpdates(e: ElevationUpdaterClient) {
        updater.cancelElevationUpdates(e)
    }

    override fun getElevation(laE6: Int, loE6: Int): Short {
        return loader.getElevation(laE6, loE6)
    }

    override fun close() {
        updater.close()
        loader.close()
    }
}
