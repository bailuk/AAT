package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile

class SubTilePainter(private val scontext: ServicesInterface, private val iid: String, private val tile: Dem3Tile) : BackgroundTask() {
    override fun onInsert() {
        tile.lock()
    }


    override fun bgOnProcess(appContext: AppContext): Long {
        var size = 0L

        object : OnObject(appContext, iid, ObjTileElevation::class.java) {
            override fun run(handle: Obj) {
                val owner = handle as ObjTileElevation
                size = owner.bgOnProcessPainter(tile)
                appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, iid)
            }
        }

        return size
    }

    override fun onRemove() {
        tile.free()
        scontext.insideContext { scontext.getElevationService().requestElevationUpdates() }
    }
}
