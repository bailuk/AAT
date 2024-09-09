package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.FileTask
import ch.bailu.aat_lib.service.cache.icons.ObjImageAbstract
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap

class ObjBitmap(private val imageFile: Foc, private val syncBitmap: MapTileInterface) : ObjImageAbstract(imageFile.path) {

    override fun onInsert(sc: AppContext) {
        load(sc.services)
        sc.services.getCacheService().addToBroadcaster(this)
    }

    override fun onRemove(sc: AppContext) {
        super.onRemove(sc)
        syncBitmap.free()
    }

    private fun load(sc: ServicesInterface) {
        sc.getBackgroundService().process(BitmapLoader(imageFile))
    }

    override fun getSize(): Long {
        return syncBitmap.getSize()
    }

    override fun isReadyAndLoaded(): Boolean {
        return syncBitmap.isLoaded()
    }

    override fun getBitmap(): Bitmap? {
        return syncBitmap.getBitmap()
    }

    class Factory : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjBitmap(appContext.toFoc(id), appContext.createMapTile())
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
                        self.syncBitmap.set(self.imageFile, 0, false)
                        size = self.syncBitmap.getSize()
                    } catch (e: Exception) {
                        self.setException(e)
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
}
