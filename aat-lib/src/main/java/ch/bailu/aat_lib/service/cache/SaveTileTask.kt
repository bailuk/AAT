package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.logger.AppLog.w
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.foc.Foc
import java.io.OutputStream

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
        var out: OutputStream? = null
        val file = getFile()
        if (!file.exists()) {
            try {
                out = file.openW()
                val bitmap = self.getTileBitmap()
                if (bitmap != null && out != null) {
                    bitmap.compress(out)
                }
                sc.broadcaster.broadcast(
                    AppBroadcaster.FILE_CHANGED_ONDISK,
                    getFile().toString(), sourceID
                )
                size = self.getSize()
            } catch (e: Exception) {
                w(this, e)
            } finally {
                Foc.close(out)
            }
        }
        return size
    }
}
