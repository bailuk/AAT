package ch.bailu.aat.services.tileremover;

import java.io.IOException;

import ch.bailu.aat.preferences.map.SolidTileCacheDirectory;
import ch.bailu.aat.util.AppBroadcaster;

public class StateUnscanned implements State {

    private final StateMachine state;


    public StateUnscanned(StateMachine s) {
        state = s;
        reset();
    }

    @Override
    public void scan() {
        state.set(new StateScan(state));
    }

    @Override
    public void stop() {}

    @Override
    public void reset() {
        state.list = null;
        state.baseDirectory = new SolidTileCacheDirectory(state.context).getValueAsFile();
        try {
            state.summaries.rescanKeep(state.context, state.baseDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        state.broadcast(AppBroadcaster.TILE_REMOVER_STOPPED);
    }


    @Override
    public void remove() { state.set(new StateScan(state));}

    @Override
    public void removeAll() {
        state.set(new StateRemoveAll(state));
    }

    @Override
    public void rescan() {}
}
