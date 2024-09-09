package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.preferences.system.SolidCacheSize
import ch.bailu.aat_lib.service.VirtualService
import ch.bailu.aat_lib.util.MemSize
import ch.bailu.aat_lib.util.WithStatusText

class CacheService(val appContext: AppContext) : VirtualService(), CacheServiceInterface,
    OnPreferencesChanged, WithStatusText {

    private val table: ObjectTable = ObjectTable()
    private val broadcaster: ObjectBroadcaster = ObjectBroadcaster(appContext)

    private val solidCacheSize = SolidCacheSize(appContext.storage)

    private val onFileProcessed: BroadcastReceiver = BroadcastReceiver{ args ->
        table.onObjectChanged(this@CacheService, *args)
    }

    init {
        solidCacheSize.register(this)
        table.limit(this, solidCacheSize.valueAsLong)
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileProcessed)
    }

    override fun onLowMemory() {
        table.limit(this, MemSize.MB)
        solidCacheSize.index = 1
    }

    override fun getObject(path: String, factory: Obj.Factory): Obj {
        return table.getHandle(path, factory, this)
    }

    override fun getObject(id: String): Obj {
        return table.getHandle(id)
    }

    override fun appendStatusText(builder: StringBuilder) {
        table.appendStatusText(builder)
    }

    override fun close() {
        appContext.broadcaster.unregister(onFileProcessed)

        solidCacheSize.unregister(this)
        broadcaster.close()
        table.close(this)
    }

    override fun addToBroadcaster(obj: ObjBroadcastReceiver) {
        broadcaster.put(obj)
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {
        if (solidCacheSize.hasKey(key)) {
            table.limit(this, solidCacheSize.valueAsLong)
        }
    }
}
