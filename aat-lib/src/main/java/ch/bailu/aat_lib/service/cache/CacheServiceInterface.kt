package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.util.WithStatusText

interface CacheServiceInterface : WithStatusText {
    fun onLowMemory()
    fun close()
    fun getObject(path: String, factory: Obj.Factory): Obj
    fun addToBroadcaster(obj: ObjBroadcastReceiver)
    fun getObject(id: String): Obj
}
