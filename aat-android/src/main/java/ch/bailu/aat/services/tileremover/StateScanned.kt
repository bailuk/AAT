package ch.bailu.aat.services.tileremover;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public final class StateScanned implements State {
    private final StateMachine state;

    public StateScanned(StateMachine s) {
        state = s;
        state.broadcast(AppBroadcaster.TILE_REMOVER_STOPPED);
    }


    @Override
    public void scan() {
        rescan();
    }


    @Override
    public void rescan() {
        state.set(new StateScanForRemoval(state));
    }


    @Override
    public void stop() {}

    @Override
    public void reset() {
        state.set(new StateUnscanned(state));
    }



    @Override
    public void remove() {
        if (state.summaries.get(0).countToRemove > 0) {
            state.set(new StateRemove(state));
        }
    }

    @Override
    public void removeAll() {
        state.set(new StateRemoveAll(state));
    }

}
