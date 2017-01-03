package ch.bailu.aat.services.tileremover;

import ch.bailu.aat.util.AppBroadcaster;

public class StateUnscanned implements State {

    private final StateMachine state;


    public StateUnscanned(StateMachine s) {
        state = s;

        state.list = null;
        state.summaries.reset(s.context);

        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_STOPPED);
    }



    @Override
    public void scan() {
        state.set(new StateScan(state));
    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {}

    @Override
    public void resetAndRescan() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void rescan() {

    }
}
