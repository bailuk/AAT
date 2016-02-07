package ch.bailu.aat.services.tracker;

import java.io.Closeable;
import java.io.IOException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.tracker.location.LocationStack;

public class TrackerInternals 
implements OnSharedPreferenceChangeListener, Closeable ,Runnable {

    private final static int TIMEOUT = 1000;
    private Timer timer;
    
    
    public final AbsService serviceContext;
    public State state=null;

    
    
    public Logger logger;
    public LocationStack location;

    public StatusIcon statusIcon;
    public Backlight backlight;
    
    
    private Storage storage;
    public SolidAutopause sautopause;
    public SolidBacklight sbacklight;
        
    public int presetIndex;

    
    public void rereadPreferences() {
        presetIndex = new SolidPreset(serviceContext).getIndex();
        sbacklight = new SolidBacklight(serviceContext, presetIndex);
        sautopause = new SolidAutopause(serviceContext, presetIndex);
    }

        
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {

        location.preferencesChanged(serviceContext, presetIndex);
        state.preferencesChanged();
    }


        
    
    
    
    public TrackerInternals(AbsService s) {
        serviceContext = s;
        
        backlight = new Backlight(serviceContext);
        location = new LocationStack(serviceContext);

        storage = Storage.preset(serviceContext);
        storage.register(this);
        rereadPreferences();

        statusIcon = new StatusIcon(serviceContext);
      
        try {
            logger = createLogger();
            logger.close();
        } catch (IOException e) {}
        logger = Logger.createNullLogger();

        state = new OffState(this);
        timer = new Timer(this, TIMEOUT);
        run();
    }
    
    
    public void lockService() {
        serviceContext.startService(new Intent(serviceContext, TrackerService.class));
    }

    public void unlockService() {
        serviceContext.stopSelf();
    }    
    
    
    
 
    
    public  TrackLogger createLogger() throws IOException {
        return new TrackLogger(serviceContext,new SolidPreset(serviceContext).getIndex());
    }
    
    
 
    
    public boolean isReadyForAutoPause() {
        return (   (location.isMissingUpdates() || location.isAutopaused())
                && sautopause.isEnabled() ); 
    }

    
    public void emergencyOff(Exception e) {
        AppLog.e(this,e);
        logger.close();
        logger = Logger.createNullLogger();
        state = new OffState(this);
    }


    @Override
    public void close() {
        logger.close();
        timer.close();

        storage.unregister(this);            

        location.close();
        
    }
    
    
    @Override
    public void run() {
        timer.kick();
        state.onTimer();
    }
}
