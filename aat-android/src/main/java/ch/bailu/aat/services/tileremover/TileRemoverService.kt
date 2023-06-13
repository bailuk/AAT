package ch.bailu.aat.services.tileremover

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.service.VirtualService

class TileRemoverService(val serviceContext: ServiceContext) : VirtualService() {
    private val state: StateMachine
    private var locked = false

    val context: Context
        get() = serviceContext.context

    private val onRemove: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            lock()
        }
    }
    private val onStop: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            free()
        }
    }

    init {
        AndroidBroadcaster.register(serviceContext.context, onStop, AppBroadcaster.TILE_REMOVER_STOPPED)
        AndroidBroadcaster.register(serviceContext.context, onRemove, AppBroadcaster.TILE_REMOVER_REMOVE)
        state = StateMachine(serviceContext)
    }

    private fun lock() {
        if (!locked) {
            locked = true
            serviceContext.lock(TileRemoverService::class.java.simpleName)
        }
    }

    private fun free() {
        if (locked) {
            locked = false
            serviceContext.free(TileRemoverService::class.java.simpleName)
        }
    }

    fun close() {
        context.unregisterReceiver(onRemove)
        context.unregisterReceiver(onStop)
        state.reset()
    }

    fun getState(): State {
        return state
    }

    val info: SelectedTileDirectoryInfo
        get() = state.info
    val summaries: SourceSummaries
        get() = state.summaries
}
