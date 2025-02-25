package ch.bailu.aat_lib.service.cache

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.broadcaster.BroadcastData.getFile
import ch.bailu.aat_lib.broadcaster.BroadcastData.getUrl
import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.util.IndexedMap
import java.io.Closeable


class ObjectBroadcaster(private val appContext: AppContext) : Closeable {
    private val table = IndexedMap<String, ObjBroadcastReceiver>()

    private val onFileChanged = BroadcastReceiver { args -> sendOnChanged(getFile(args)) }

    private val onFileDownloaded = BroadcastReceiver { args ->
        sendOnDownloaded(
            getFile(args), getUrl(args)
        )
    }

    init {
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileChanged)
        appContext.broadcaster.register(AppBroadcaster.FILE_CHANGED_ONDISK, onFileDownloaded)
    }

    @Synchronized
    fun put(b: ObjBroadcastReceiver) {
        table.put(b.toString(), b)
    }

    @Synchronized
    fun delete(b: ObjBroadcastReceiver) {
        delete(b.toString())
    }

    @Synchronized
    fun delete(id: String) {
        table.remove(id)
    }

    override fun close() {
        appContext.broadcaster.unregister(onFileDownloaded)
        appContext.broadcaster.unregister(onFileChanged)
    }

    @Synchronized
    private fun sendOnChanged(id: String) {
        for (i in 0 until table.size()) {
            table.getValueAt(i)?.onChanged(id, appContext)
        }
    }

    @Synchronized
    private fun sendOnDownloaded(id: String, uri: String) {
        for (i in 0 until table.size()) {
            table.getValueAt(i)?.onDownloaded(id, uri, appContext)
        }
    }
}
