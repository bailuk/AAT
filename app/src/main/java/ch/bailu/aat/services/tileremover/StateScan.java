package ch.bailu.aat.services.tileremover;

import java.io.File;
import java.io.IOException;

import ch.bailu.aat.preferences.SolidTileCacheDirectory;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.ui.AppLog;


public class StateScan implements State, Runnable {
    private final StateMachine state;
    private Class nextState = StateScanForRemoval.class;

    public StateScan(StateMachine s) {
        state = s;
        state.tileDirectory = new SolidTileCacheDirectory(s.context).getValueAsFile();

        state.list = new TilesList();

        state.summaries.reset(s.context);

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
        scanRootDirectoryTree(state.tileDirectory);

        if(keepUp()) {
            broadcast();
        }
        state.list.log();
        state.setFromClass(nextState);
    }



    private boolean keepUp() {
        return (nextState == StateScanForRemoval.class);
    }

    private void broadcast() {
        AppBroadcaster.broadcast(state.context, AppBroadcaster.TILE_REMOVER_SCAN);
    }




    private void scanRootDirectoryTree(File file) {
        try {
            scanRootDirectories(file, state.summaries);
            for (int i=1; i< SourceSummaries.SUMMARY_SIZE; i++) {
                String name = state.summaries.getMapSummary()[i].getName();

                if (name != null && name.length()>0 ) {
                    File directory = new File(file.getCanonicalFile(), name);
                    if (doDirectory(directory) && keepUp()) {
                        scanMapDirectory(directory, i);
                    }
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            AppLog.e(state.context, e);
        }
    }

    private void scanMapDirectory(File directory, int summary) {

        final File[] files = directory.listFiles();




        if (files != null) {
            for (File file : files) {
                if (doDirectory(file) && keepUp()) {
                    try {
                        scanZoomDirectory(file, summary);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }

    }

    private void scanZoomDirectory(File directory, int summary)
            throws  NumberFormatException {
        final File[] files = directory.listFiles();

        final short zoom = TileFile.getZoom(directory);

        if (files != null) {
            for (File file: files) {
                if (doDirectory(file) && keepUp() ) {
                    broadcast();
                    scanTileDirectory(file, zoom, summary);
                }
            }
        }
    }

    private void scanTileDirectory(File directory, short zoom, int summary)
            throws NumberFormatException{

        final File[] files = directory.listFiles();
        final int x = TileFile.getX(directory);

        if (files != null) {
            for (File file : files) {
                processFile(file, zoom, x, summary);
            }
        }
    }

    public static void scanRootDirectories(File tileCacheDirectory, SourceSummaries summaries) throws IOException {
        tileCacheDirectory = tileCacheDirectory.getCanonicalFile();

        final File[] files = tileCacheDirectory.listFiles();

        if (files != null) {
            int f = 0;
            int summaryIndex = 1;

            while (f < files.length && summaryIndex < SourceSummaries.SUMMARY_SIZE) {
                if (doDirectory(files[f])) {
                    summaries.setName(summaryIndex, files[f].getName());
                    summaryIndex++;
                }

                f++;
            }
        }
    }

    private static boolean doDirectory(File file) {
        return file.isDirectory() && !file.isHidden() && isReal(file);
    }


    private static boolean isReal(File file) {
        try {
            final String c = file.getCanonicalPath();
            final String a = file.getAbsolutePath();

            return c.equals(a);

        } catch (IOException e) {
            return false;
        }
    }



    private void processFile(File file, short zoom, int x, int summary) {
        try {
            final TileFile tile = new TileFile(summary, zoom, x, file);
            state.list.add(tile);
            state.summaries.addFile(summary, tile);
        } catch (NumberFormatException e) {}
    }
}
