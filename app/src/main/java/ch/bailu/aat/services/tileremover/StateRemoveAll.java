package ch.bailu.aat.services.tileremover;


import java.io.File;

import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.JFile;
import ch.bailu.aat.util.ui.AppLog;

public class StateRemoveAll implements State, Runnable{

    private final StateMachine state;
    private Class nextState = StateRemoved.class;



    public StateRemoveAll(StateMachine s) {
        state = s;

        new Thread(this).start();
    }

    @Override
    public void stop() {
        nextState = StateUnscanned.class;
    }


    @Override
    public void reset() {
        nextState = StateUnscanned.class;
    }


    @Override
    public void scan() {}


    @Override
    public void remove() {}

    @Override
    public void rescan() {}

    @Override
    public void removeAll() {}

    @Override
    public void run() {
        SelectedTileDirectoryInfo info = state.getInfo();

        TileScanner scanner = new TileScanner(info.directory) {
            int sourceIndex=0;

            @Override
            protected boolean doSourceContainer(File dir) {
                return keepUp();
            }

            @Override
            protected boolean doZoomContainer(File dir) {
                sourceIndex = state.summaries.findIndex(source);
                return keepUp();
            }

            @Override
            protected boolean doXContainer(File dir) {
                return keepUp();
            }

            @Override
            protected boolean doYContainer(File dir) {
                return keepUp();
            }

            @Override
            protected void doFile(File file) {
                delete(file, new TileFile(sourceIndex, zoom, x, y, file));
            }
        };

        if (info.index == 0) {
            scanner.scanSourceContainer();
        } else {
            scanner.scanZoomContainer();
        }

        if (keepUp()) {
            JFile.deleteEmptiyDirectoriesRecursive(info.directory);
            broadcast();
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

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_REMOVE);
    }
}
