package ch.bailu.aat.services.cache

import ch.bailu.aat.util.graphic.SyncBitmap
import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap

class ObjBitmap(private val imageFile: Foc) : ObjImageAbstract(imageFile.path) {
    private val syncBitmap = SyncBitmap()

    private constructor() : this(Foc.FOC_NULL)

    override fun onInsert(sc: AppContext) {
        load(sc.services)
        sc.services.cacheService.addToBroadcaster(this)
    }

    override fun onRemove(sc: AppContext) {
        super.onRemove(sc)
        syncBitmap.free()
    }

    private fun load(sc: ServicesInterface) {
        sc.backgroundService.process(BitmapLoader(imageFile))
    }

    override fun getSize(): Long {
        return syncBitmap.size.toLong()
    }

    override fun isReadyAndLoaded(): Boolean {
        return syncBitmap.bitmap != null
    }

    override fun getBitmap(): Bitmap? {
        return syncBitmap.bitmap
    }

    class Factory : Obj.Factory() {
        override fun factory(id: String, sc: AppContext): Obj {
            return ObjBitmap(sc.toFoc(id))
        }
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {
        if (id == toString()) {
            load(sc.services)
        }
    }

    override fun onChanged(id: String, sc: AppContext) {}
    private class BitmapLoader(f: Foc) : FileTask(f) {

        override fun bgOnProcess(appContext: AppContext): Long {

            var size = 0L
            object : OnObject(appContext, toString(), ObjBitmap::class.java) {
                override fun run(obj: Obj) {
                    val self = obj as ObjBitmap
                    try {
                        self.syncBitmap.set(self.imageFile)
                        size = self.syncBitmap.size.toLong()
                    } catch (e: Exception) {
                        self.exception = e
                    }
                    appContext.broadcaster.broadcast(
                        AppBroadcaster.FILE_CHANGED_INCACHE,
                        self.imageFile.path
                    )
                }
            }
            return size
        }
    }

    companion object {
        val NULL = ObjBitmap()
    }
}
