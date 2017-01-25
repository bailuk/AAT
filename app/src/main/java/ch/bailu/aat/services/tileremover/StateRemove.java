package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.util.Iterator;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;

public class StateRemove implements State, Runnable {
    private static final int BROADCAST_INTERVAL =5;
    private int broadcast_count = 0;

    private final StateMachine state;
    private Class nextState = StateRemoved.class;


    public StateRemove(StateMachine s) {
        state = s;

        new Thread(this).start();
    }




    @Override
    public void scan() {}

    @Override
    public void stop() {
        nextState = StateScanned.class;
    }


    @Override
    public void reset() {
        nextState = StateUnscanned.class;
    }

    @Override
    public void resetAndRescan() {
        nextState = StateScan.class;
    }

    @Override
    public void remove() {

    }

    @Override
    public void rescan() {

    }

    @Override
    public void run() {
        final Iterator<TileFile> iterator = state.list.iteratorToRemove();


        while (iterator.hasNext() && keepUp()) {
            final TileFile t = iterator.next();
            final File f = state.summaries.toFile(state.tileDirectory, t);

            broadcast();
            delete(f, t);
        }

        state.list.resetToRemove();
        if(keepUp()) broadcast();
        state.setFromClass(nextState);
    }

    private boolean delete(File f, TileFile t) {
        if (f.delete()) {
            state.summaries.addFileRemoved(t);
            return true;
        }


        AppLog.d(this, "Failed to delete: " + f.toString());
        return false;
    }


    private void broadcastIntervalled() {
        if (broadcast_count <= 0) {
            broadcast_count = BROADCAST_INTERVAL;
            broadcast();
        } else {
            broadcast_count--;
        }
    }

    private boolean keepUp() {
        return (nextState == StateRemoved.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_REMOVE);
    }
}
