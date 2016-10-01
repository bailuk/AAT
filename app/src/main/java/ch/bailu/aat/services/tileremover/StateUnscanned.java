package ch.bailu.aat.services.tileremover;

import java.io.File;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;

public class StateUnscanned implements State {

    private final StateMachine state;


    public StateUnscanned(StateMachine s) {
        state = s;

        state.list = null;
        state.summaries.reset();

        state.freeService();
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
