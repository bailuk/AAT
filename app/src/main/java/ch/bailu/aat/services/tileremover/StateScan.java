package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;


public class StateScan implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanForRemoval.class;

    public StateScan(StateMachine s) {
        state = s;
        state.tileDirectory = new SolidTileCacheDirectory(s.context).getValueAsFile();

        state.list = new TilesList();
        state.summaries.reset(s.context);

        state.lockService();
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
    public void resetAndRescan() {
        nextState = StateScan.class;
    }

    @Override
    public void remove() {}

    @Override
    public void rescan() {}

    @Override
    public void run() {
        scanRootDirectory(state.tileDirectory);

        if(keepUp()) {
            broadcast();
        }

        state.setFromClass(nextState);
    }



    private boolean keepUp() {
        return (nextState == StateScanForRemoval.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_SCAN);
    }


    private void scanRootDirectory(File file) {
        try {
            file = file.getCanonicalFile();

            final File[] files = file.listFiles();

            if (files != null) {
                int f = 0;
                int s = 1;

                while (f < files.length && s < MapSummaries.SUMMARY_SIZE && keepUp()) {
                    if (doDirectory(files[f]) && keepUp()) {


                        state.summaries.setName(s, files[f].getName());
                        scanMapDirectory(files[f], s);
                        s++;
                    }

                    f++;
                }
            }

        } catch (IOException e) {
            AppLog.e(state.context, e);
        }
    }

    private void scanMapDirectory(File file, int summary) {

        final File[] files = file.listFiles();

        final int hash=TileFile.getBaseDirHash(file);


        if (files != null) {
            for (File file1 : files) {
                if (doDirectory(file1) && keepUp()) {
                    try {
                        scanZoomDirectory(file1, hash, summary);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

    }

    private void scanZoomDirectory(File file, int hash, int summary)
            throws  NumberFormatException {
        final File[] files = file.listFiles();

        final short zoom = TileFile.getZoom(file);

        if (files != null) {
            for (int i=0; i<files.length; i++) {
                if (doDirectory(files[i]) && keepUp() ) {
                    broadcast();
                    scanTileDirectory(files[i], hash, zoom, summary);
                }
            }
        }
    }

    private void scanTileDirectory(File file, int hash, short zoom, int summary)
            throws NumberFormatException{

        final File[] files = file.listFiles();
        final int x = TileFile.getX(file);

        if (files != null) {
            for (File f : files) {
                processFile(f, hash, zoom, x, summary);
            }
        }
    }


    private boolean doDirectory(File file) {
        return file.isDirectory() && !file.isHidden() && isReal(file);
    }


    private boolean isReal(File file) {
        try {
            final String c = file.getCanonicalPath();
            final String a = file.getAbsolutePath();

            return c != null &&  c.equals(a);

        } catch (IOException e) {
            return false;
        }
    }



    private void processFile(File file, int hash, short zoom, int x, int summary) {
        try {
            final TileFile tile = new TileFile(hash, zoom, x, file);
            state.list.add(tile);
            state.summaries.addFile(summary, tile);
        } catch (NumberFormatException e) {}
    }
}
