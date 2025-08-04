package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap

class SaveTileTask(private val sourceID: String, target: Foc) : FileTask(target) {
    override fun bgOnProcess(appContext: AppContext): Long {
        var size = 0L
        object : OnObject(appContext, sourceID, ObjTile::class.java) {
            override fun run(handle: Obj) {
                size = save(appContext, handle as ObjTile)
            }
        }
        return size
    }

    private fun save(sc: AppContext, self: ObjTile): Long {
        var size = 0L
        val file = getFile()
        val bitmap = self.getTileBitmap()

        if (!file.exists() && bitmap is Bitmap) {
            try {
                file.openW().use {
                    bitmap.compress(it)
                    sc.broadcaster.broadcast(
                        AppBroadcaster.FILE_CHANGED_ONDISK,
                        getFile().toString(), sourceID
                    )
                    size = self.getSize()
                }
            } catch (e: Exception) {
                AppLog.w(this, e)
            }
        }
        return size
    }
}
