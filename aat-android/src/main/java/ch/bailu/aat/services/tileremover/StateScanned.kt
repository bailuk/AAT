package ch.bailu.aat.services.tileremover

import ch.bailu.aat_lib.dispatcher.AppBroadcaster

class StateScanned(private val state: StateMachine) : State {
    init {
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

    override fun remove() {
        if (state.summaries[0].countToRemove > 0) {
            state.set(StateRemove(state))
        }
    }

    override fun removeAll() {
        state.set(StateRemoveAll(state))
    }
}
