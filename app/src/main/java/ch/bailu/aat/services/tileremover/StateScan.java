package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.preferences.SolidTileCacheDirectory;


public class StateScan implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanForRemoval.class;

    public StateScan(StateMachine s) {
        state = s;
        state.tileDirectory = new SolidTileCacheDirectory(s.context).toFile();

        state.list.reset();
        state.summaries.reset();

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
        final File[] files = file.listFiles();

        if (files != null) {
            int f = 0;
            int s = 1;

            while (f < files.length && s < TilesSummaries.SUMMARY_SIZE && keepUp()) {
                if (doDirectory(files[f]) && keepUp()) {


                    state.summaries.setName(s, files[f].getName());
                    scanSubRootDirectory(files[f], s);
                    s++;
                }

                f++;
            }
        }

    }

    private void scanSubRootDirectory(File file, int summary) {
        final File[] files = file.listFiles();

        final int hash=TileFile.getBaseDirHash(file);


        if (files != null) {
            for (int i=0; i<files.length; i++) {
                if (doDirectory(files[i]) && keepUp() ) {
                    scanZoomDirectory(files[i], hash, summary);
                }
            }
        }

    }

    private void scanZoomDirectory(File file, int hash, int summary) {
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

    private void scanTileDirectory(File file, int hash, short zoom, int summary) {

        final File[] files = file.listFiles();
        final int x = TileFile.getX(file);

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                processFile(files[i], hash, zoom, x, summary);
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
        final TileFile tile = new TileFile(hash, zoom, x, file);
        state.list.add(tile);
        state.summaries.inc(summary, tile);

    }
}
