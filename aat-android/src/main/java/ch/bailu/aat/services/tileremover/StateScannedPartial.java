package ch.bailu.aat.services.tileremover;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public final class StateScannedPartial implements State {

    private final StateMachine state;

    public StateScannedPartial(StateMachine s) {
        state = s;

        state.list.resetToRemove();
        state.summaries.resetToRemove();
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
    public void remove() {}

    @Override
    public void removeAll() {state.set(new StateRemoveAll(state));}
}
