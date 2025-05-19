package ch.bailu.aat_lib.service.elevation.updater

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile

interface ElevationUpdaterClient {
    fun updateFromSrtmTile(appContext: AppContext, tile: Dem3Tile)
}
