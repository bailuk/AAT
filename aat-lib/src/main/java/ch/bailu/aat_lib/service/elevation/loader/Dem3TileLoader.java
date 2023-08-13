package ch.bailu.aat_lib.service.elevation.loader;


import org.jetbrains.annotations.NotNull;

import java.io.Closeable;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidDem3EnableDownload;
import ch.bailu.aat_lib.service.background.DownloadTask;
import ch.bailu.aat_lib.service.elevation.tile.Dem3Tile;
import ch.bailu.aat_lib.util.Timer;
import ch.bailu.foc.Foc;

public final class Dem3TileLoader implements Closeable {
    private static final long MILLIS = 2000;

    private final AppContext appContext;
    private final Dem3Tiles tiles;

    private Dem3Coordinates pending = null;

    private final Timer timer;

    private final SolidDem3EnableDownload sdownload;

    public Dem3TileLoader(AppContext appContext, Timer timer, Dem3Tiles tiles) {
        this.timer = timer;
        this.tiles = tiles;
        this.appContext = appContext;
        this.appContext.getBroadcaster().register(onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);

        this.sdownload = new SolidDem3EnableDownload(appContext.getStorage());
        this.sdownload.register(onPreferencesChanged);
    }

    private final Runnable timeout = () -> {
        if (havePending()) {
            loadOrDownloadPending();
            stopTimer();
        }
    };

    private final BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {
        @Override
        public void onReceive(@NotNull String... args) {
            String id = BroadcastData.getFile(args);
            Dem3Tile tile = tiles.get(id);

            if (tile != null) {
                tile.reload(appContext.getServices(), appContext.getDem3Directory());
            }
        }
    };

    private final OnPreferencesChanged onPreferencesChanged = new OnPreferencesChanged() {
        @Override
        public void onPreferencesChanged(@Nonnull StorageInterface storage, @Nonnull String key) {
            if (sdownload.hasKey(key) && sdownload.getValue()) {
                tiles.removeEmpty(); // Reset for re-download
            }
        }
    };

    private void loadOrDownloadPending() {
        final Dem3Coordinates toLoad = pending;
        pending = null;

        if (toLoad != null) {
            loadNow(toLoad);

            if (sdownload.getValue()) {
                downloadNow(toLoad);
            }
        }
    }

    public Dem3Tile loadNow(Dem3Coordinates coordinates) {
        if (havePending()) cancelPending();
        return loadIntoOldestSlot(coordinates);
    }

    private Dem3Tile loadIntoOldestSlot(Dem3Coordinates coordinates) {
        if (!tiles.have(coordinates)) {
            final Dem3Tile slot = tiles.getOldestProcessed();

            if (slot != null && !slot.isLocked()) {
                slot.load(appContext.getServices(), coordinates,  appContext.getDem3Directory());
            }
        }

        return tiles.get(coordinates);
    }

    public void loadOrDownloadLater(Dem3Coordinates coordinates) {
        if (pending == null) { // first BitmapRequest
            startTimer();
        }
        pending = coordinates;
    }

    public void cancelPending() {
        pending = null;
        stopTimer();
    }

    private boolean havePending() {
        return pending != null;
    }

    private void startTimer() {
        timer.cancel();
        timer.kick(MILLIS, timeout);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void downloadNow(Dem3Coordinates c) {
        Foc file = appContext.getDem3Directory().toFile(c);
        if (!file.exists()) {
            DownloadTask handle = new DownloadTask(c.toURL(), file, appContext.getDownloadConfig());
            appContext.getServices().getBackgroundService().process(handle);
        }
    }

    @Override
    public void close() {
        appContext.getBroadcaster().unregister(onFileDownloaded);
        sdownload.unregister(onPreferencesChanged);
    }
}
