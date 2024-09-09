package ch.bailu.aat_lib.service.cache.icons

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.map.tile.MapTileInterface
import ch.bailu.aat_lib.service.ServicesInterface
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject
import ch.bailu.foc.Foc
import org.mapsforge.core.graphics.Bitmap
import java.io.InputStream

class ObjSVGAsset(id: String, private val name: String, private val size: Int) : ObjImageAbstract(id) {
    private var bitmap: MapTileInterface? = null

    override fun onInsert(appContext: AppContext) {
        load(appContext.services)
    }

    private fun load(sc: ServicesInterface) {
        sc.getBackgroundService().process(SvgLoader(getID()))
    }

    override fun getBitmap(): Bitmap? {
        val b = bitmap
        if (b != null) {
            return b.getBitmap()
        }
        return null
    }

    override fun getSize(): Long {
        var result: Long = 0

        val b = bitmap
        if (b != null) {
            result = b.getSize()
        }

        if (result == 0L) {
            result = MIN_SIZE.toLong()
        }

        return result
    }

    override fun onDownloaded(id: String, url: String, sc: AppContext) {}

    override fun onChanged(id: String, sc: AppContext) {}

    override fun isReadyAndLoaded(): Boolean {
        return getBitmap() != null
    }

    override fun onRemove(appContext: AppContext) {
        super.onRemove(appContext)
        val b = bitmap
        b?.free()
    }

    class Factory(private val name: String, private val size: Int) : Obj.Factory() {
        override fun factory(id: String, appContext: AppContext): Obj {
            return ObjSVGAsset(id, name, size)
        }
    }

    private class SvgLoader(private val id: String) : BackgroundTask() {
        override fun bgOnProcess(appContext: AppContext): Long {
            val size = longArrayOf(0)

            object : OnObject(appContext, id, ObjSVGAsset::class.java) {
                override fun run(obj: Obj) {
                    val self = obj as ObjSVGAsset

                    var input: InputStream? = null
                    if (self.bitmap == null) {
                        self.bitmap = appContext.createMapTile()
                    }

                    try {
                        input = appContext.assets.toFoc(self.name).openR()
                        self.bitmap?.setSVG(appContext.assets.toFoc(self.name), self.size, true)
                        size[0] = self.size.toLong()
                    } catch (e: Exception) {
                        self.setException(e)
                    } finally {
                        Foc.close(input)
                    }

                    appContext.broadcaster.broadcast(AppBroadcaster.FILE_CHANGED_INCACHE, id)
                }
            }
            return size[0]
        }
    }

    companion object {
        fun toID(name: String, size: Int): String {
            if (name.isNotEmpty()) return ObjSVGAsset::class.java.simpleName + "/" + name + "/" + size
            return ""
        }
    }
}
