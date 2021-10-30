package ch.bailu.aat_lib.service.tracker;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.SolidAutopause;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.presets.SolidPreset;
import ch.bailu.aat_lib.preferences.presets.SolidTrackerAutopause;
import ch.bailu.aat_lib.preferences.system.SolidDataDirectory;
import ch.bailu.aat_lib.service.ServicesInterface;

public final class TrackerInternals
        implements OnPreferencesChanged, Closeable {

    private State state;

    public Logger logger;

    public final StatusIconInterface statusIcon;

    public final ServicesInterface services;

    public SolidAutopause sautopause;

    public int presetIndex;

    public final Broadcaster broadcaster;

    private final SolidDataDirectory sdirectory;

    public TrackerInternals(SolidDataDirectory sdirectory, StatusIconInterface statusIconInterface, Broadcaster broadcastInterface, ServicesInterface services) {

        this.sdirectory = sdirectory;
        this.services = services;
        broadcaster = broadcastInterface;
        statusIcon = statusIconInterface;

        rereadPreferences();


        try {
            logger = createLogger();
            logger.close();
        } catch (Exception e) {
            AppLog.e(e);
        }
        logger = Logger.NULL_LOGGER;

        state = new OffState(this);
        sdirectory.getStorage().register(this);

    }


    public void setState(State state) {
        this.state = state;
        broadcaster.broadcast(AppBroadcaster.TRACKER);
    }

    public State getState() {
        return state;
    }


    public void rereadPreferences() {
        presetIndex = new SolidPreset(sdirectory.getStorage()).getIndex();
        sautopause = new SolidTrackerAutopause(sdirectory.getStorage(), presetIndex);

        services.getLocationService().setPresetIndex(presetIndex);
    }


    @Override
    public void onPreferencesChanged(StorageInterface storage, String key) {
        state.preferencesChanged();
    }








    public void lockService() {
        services.lock(this.getClass().getSimpleName());
    }

    public void unlockService() {
        services.free(this.getClass().getSimpleName());
    }





    public  TrackLogger createLogger() throws IOException, SecurityException {
        return new TrackLogger(sdirectory, new SolidPreset(sdirectory.getStorage()).getIndex());
    }




    public boolean isReadyForAutoPause() {
        return ((
                services.getLocationService().isMissingUpdates() ||
                        services.getLocationService().isAutopaused())    &&
                sautopause.isEnabled());
    }


    public void emergencyOff(Exception e) {
        AppLog.e(this,e);
        logger.close();
        logger = Logger.NULL_LOGGER;
        state = new OffState(this);
    }


    @Override
    public void close() {
        logger.close();

        sdirectory.getStorage().unregister(this);
    }


}
