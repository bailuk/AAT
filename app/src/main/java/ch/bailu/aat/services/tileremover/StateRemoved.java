package ch.bailu.aat.services.tileremover;

import java.io.File;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;

public class StateRemoved implements State {
    private final StateMachine state;



    public StateRemoved(StateMachine s) {
        state = s;

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
    public void reset() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void rescan() {

    }
}
