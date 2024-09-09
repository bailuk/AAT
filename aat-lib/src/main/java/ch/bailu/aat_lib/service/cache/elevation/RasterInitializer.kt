package ch.bailu.aat_lib.service.cache.elevation

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.service.background.BackgroundTask
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.OnObject

class RasterInitializer(private val iid: String) : BackgroundTask() {
    private var size: Long = 0

    override fun bgOnProcess(appContext: AppContext): Long {
        size = 0

        object : OnObject(appContext, iid, ObjTileElevation::class.java) {
            override fun run(obj: Obj) {
                val owner = obj as ObjTileElevation
                size = owner.bgOnProcessInitializer(appContext)
            }
        }
        return size
    }
}
