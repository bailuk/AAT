package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.app.AppContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.service.VirtualService

// TODO move to lib
class TileRemoverService(val appContext: AppContext) : VirtualService() {
    private var locked = false

    val state: StateMachine

    val info: SelectedTileDirectoryInfo
        get() = state.info

    val summaries: SourceSummaries
        get() = state.summaries


    private val onRemove: BroadcastReceiver = BroadcastReceiver { lock() }
    private val onStopped: BroadcastReceiver = BroadcastReceiver {free() }

    init {
        appContext.broadcaster.register(AppBroadcaster.TILE_REMOVER_STOPPED, onStopped)
        appContext.broadcaster.register(AppBroadcaster.TILE_REMOVER_REMOVE, onRemove)

        state = StateMachine(appContext)
    }

    private fun lock() {
        if (!locked) {
            locked = true
            appContext.services.lock(TileRemoverService::class.java.simpleName)
        }
    }

    private fun free() {
        if (locked) {
            locked = false
            appContext.services.free(TileRemoverService::class.java.simpleName)
        }
    }

    fun close() {
        appContext.broadcaster.unregister(onRemove)
        appContext.broadcaster.unregister(onStopped)
        state.reset()
    }
}
