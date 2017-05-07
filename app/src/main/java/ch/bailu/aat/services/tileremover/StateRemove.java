package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.util.Iterator;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.JFile;
import ch.bailu.aat.util.ui.AppLog;

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
        nextState = StateUnscanned.class;
    }

    @Override
    public void remove() {}

    @Override
    public void removeAll() {}

    @Override
    public void rescan() {}


    @Override
    public void run() {
        final Iterator<TileFile> iterator = state.list.iteratorToRemove();


        while (iterator.hasNext() && keepUp()) {
            final TileFile t = iterator.next();
            final File f = state.summaries.toFile(state.baseDirectory, t);

            delete(f, t);
        }

        state.list.resetToRemove();

        if (keepUp()) {
            JFile.deleteEmptiyDirectoriesRecursive(state.baseDirectory);
            state.broadcast(AppBroadcaster.TILE_REMOVER_REMOVE);
        }

        state.setFromClass(nextState);
    }

    private boolean delete(File f, TileFile t) {
        if (JFile.delete(f)) {
            state.summaries.addFileRemoved(t);
            state.broadcastLimited( AppBroadcaster.TILE_REMOVER_REMOVE);
            return true;
        }

        AppLog.d(this, "Failed to delete: " + f.toString());
        return false;
    }


    private boolean keepUp() {
        return (nextState == StateRemoved.class);
    }

}
