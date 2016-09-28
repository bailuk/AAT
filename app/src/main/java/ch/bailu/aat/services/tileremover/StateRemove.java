package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.util.Iterator;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;

public class StateRemove implements State, Runnable {
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

        int c=0;
        while (iterator.hasNext() && keepUp()) {
            final TileFile t = iterator.next();
            final File f = state.summaries.toFile(state.tileDirectory, t);

            if (c <= 0) {
                c=50;
                broadcast();
            } else {
                c--;

            }
            if (f.exists()) {
                AppLog.d(this, f.toString());
                state.summaries.inc_removed(t);
            }
        }

        state.list.resetToRemove();



        if(keepUp()) {
            broadcast();
        }

        state.setFromClass(nextState);
    }



    private boolean keepUp() {
        return (nextState == StateRemoved.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_REMOVE);
    }
}
