package ch.bailu.aat_lib.service.cache.gpx

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.gpx.GpxList
import ch.bailu.aat_lib.service.cache.Obj

abstract class ObjGpx(id: String) : Obj(id) {
    abstract fun getGpxList(): GpxList

    companion object {
        @JvmField
        val NULL: ObjGpx = object : ObjGpx(ObjGpx::class.java.simpleName) {
            override fun getSize(): Long {
                return MIN_SIZE.toLong()
            }

            override fun onChanged(id: String, appContext: AppContext) {}

            override fun onDownloaded(id: String, url: String, appContext: AppContext) {}

            override fun getGpxList(): GpxList {
                return GpxList.NULL_TRACK
            }

            override fun isReadyAndLoaded(): Boolean {
                return true
            }
        }
    }
}
