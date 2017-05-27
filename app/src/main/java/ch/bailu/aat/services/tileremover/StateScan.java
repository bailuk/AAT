package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.simpleio.foc.Foc;


public class StateScan implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanForRemoval.class;

    public StateScan(StateMachine s) {
        state = s;
        state.baseDirectory = new SolidTileCacheDirectory(s.context).getValueAsFile();

        state.list = new TilesList();
        new Thread(this).start();

    }


    @Override
    public void scan() {
        nextState = StateScanForRemoval.class;
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
    public void remove() {}

    @Override
    public void removeAll() {
        nextState = StateRemoveAll.class;
    }

    @Override
    public void rescan() {}


    @Override
    public void run() {
        scanSourceContainer(state.baseDirectory);

        if(keepUp()) {
            state.broadcast(AppBroadcaster.TILE_REMOVER_SCAN);
        }
        state.list.log();
        state.setFromClass(nextState);
    }



    private boolean keepUp() {
        return (nextState == StateScanForRemoval.class);
    }


    private void scanSourceContainer(Foc sourceContainer) {
        try {
            //sourceContainer = sourceContainer.getCanonicalFile();

            state.summaries.rescan(state.context, sourceContainer);

            for (int summaryIndex=1; summaryIndex< state.summaries.size(); summaryIndex++) {
                String sourceName = state.summaries.get(summaryIndex).getName();

                if (sourceName != null && sourceName.length()>0 ) {
                    Foc zoomContainer = sourceContainer.child(sourceName);
                    scanZoomContainer(zoomContainer, summaryIndex);
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            AppLog.e(state.context, e);
        }
    }


    private void scanZoomContainer(Foc zoomContainer, final int summaryIndex) {
        new TileScanner(zoomContainer) {

            @Override
            protected boolean doSourceContainer(Foc dir) {
                return keepUp();

            }

            @Override
            protected boolean doZoomContainer(Foc dir) {
                return keepUp();
            }

            @Override
            protected boolean doXContainer(Foc dir) {
                return keepUp();
            }


            @Override
            protected boolean doYContainer(Foc dir) {
                state.broadcastLimited(AppBroadcaster.TILE_REMOVER_SCAN);
                return keepUp();
            }


            @Override
            protected void doFile(Foc file) {
                TileFile tile = new TileFile(summaryIndex, zoom, x, file);
                state.list.add(tile);
                state.summaries.addFile(tile);
            }
        }.scanZoomContainer();
    }
}
