package ch.bailu.aat.services.tileremover;


import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.util_java.foc.Foc;

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
            protected boolean doSourceContainer(Foc dir) {
                return keepUp();
            }

            @Override
            protected boolean doZoomContainer(Foc dir) {
                sourceIndex = state.summaries.findIndex(source);
                return keepUp();
            }

            @Override
            protected boolean doXContainer(Foc dir) {
                return keepUp();
            }

            @Override
            protected boolean doYContainer(Foc dir) {
                return keepUp();
            }

            @Override
            protected void doFile(Foc file) {
                delete(file, new TileFile(sourceIndex, zoom, x, y, file));
            }
        };

        if (info.index == 0) {
            scanner.scanSourceContainer();
        } else {
            scanner.scanZoomContainer();
        }

        if (keepUp()) {
            info.directory.rmdirs();
            //MemSize.deleteEmptiyDirectoriesRecursive(info.directory);
            broadcast();
        }

        state.setFromClass(nextState);
    }



    private boolean delete(Foc f, TileFile t) {
        if (f.rm()) {
            state.summaries.addFileRemoved(t);
            state.broadcastLimited( AppBroadcaster.TILE_REMOVER_REMOVE);
            return true;
        }

        return false;
    }


    private boolean keepUp() {
        return (nextState == StateRemoved.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_REMOVE);
    }
}
