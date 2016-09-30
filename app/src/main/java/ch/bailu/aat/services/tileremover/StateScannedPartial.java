package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.helpers.AppBroadcaster;

public class StateScannedPartial implements State {

    private final StateMachine state;

    public StateScannedPartial(StateMachine s) {
        state = s;

        state.list.resetToRemove();
        state.summaries.resetToRemove();
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_STOPPED);
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
        state.set(new StateScan(state));
    }

    @Override
    public void remove() {}

}
