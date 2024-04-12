package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.broadcaster.AppBroadcaster

class StateScannedPartial(private val state: StateMachine) : State {
    init {
        state.list.resetToRemove()
        state.summaries.resetToRemove()
        state.broadcast(AppBroadcaster.TILE_REMOVER_STOPPED)
    }

    override fun scan() {
        rescan()
    }

    override fun rescan() {
        state.set(StateScanForRemoval(state))
    }

    override fun stop() {}
    override fun reset() {
        state.set(StateUnscanned(state))
    }

    override fun remove() {}
    override fun removeAll() {
        state.set(StateRemoveAll(state))
    }
}
