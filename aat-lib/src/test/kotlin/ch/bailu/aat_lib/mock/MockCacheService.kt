package ch.bailu.aat_lib.mock

import ch.bailu.aat_lib.service.cache.CacheServiceInterface
import ch.bailu.aat_lib.service.cache.Obj
import ch.bailu.aat_lib.service.cache.ObjBroadcastReceiver
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx
import java.lang.StringBuilder

class MockCacheService : CacheServiceInterface {
    override fun appendStatusText(builder: StringBuilder?) {
        TODO("Not yet implemented")
    }

    override fun onLowMemory() {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun getObject(path: String?, factory: Obj.Factory?): Obj {
        return ObjGpx.NULL

    }

    override fun getObject(id: String?): Obj {
        TODO("Not yet implemented")
    }

    override fun addToBroadcaster(obj: ObjBroadcastReceiver?) {
        TODO("Not yet implemented")
    }
}
