package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import java.io.IOException

open class StateUnscanned(private val state: StateMachine) : State {
    init {
        reset()
    }

    override fun scan() {
        state.set(StateScan(state))
    }

    override fun stop() {}

    override fun reset() {
        state.list = TilesList()
        state.baseDirectory = state.appContext.tileCacheDirectory.valueAsFile
        try {
            state.summaries.rescanKeep(state.baseDirectory)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        state.broadcast(AppBroadcaster.TILE_REMOVER_STOPPED)
    }

    override fun remove() {
        state.set(StateScan(state))
    }

    override fun removeAll() {
        state.set(StateRemoveAll(state))
    }

    override fun rescan() {}
}
