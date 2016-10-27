package ch.bailu.aat.services.tracker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.Timer;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.tracker.location.LocationStack;

public class TrackerInternals 
implements OnSharedPreferenceChangeListener, Closeable ,Runnable {

    private final static int TIMEOUT = 1000;
    private final Timer timer;
    
    
    public final ServiceContext scontext;
    public State state=null;

    
    
    public Logger logger;
    public final LocationStack location;

    public final StatusIcon statusIcon;
    public final Backlight backlight;
    
    
    private final Storage storage;
    public SolidAutopause sautopause;
    public SolidBacklight sbacklight;
        
    public int presetIndex;

    
    public void rereadPreferences() {
        presetIndex = new SolidPreset(scontext.getContext()).getIndex();
        sbacklight = new SolidBacklight(scontext.getContext(), presetIndex);
        sautopause = new SolidAutopause(scontext.getContext(), presetIndex);
    }

        
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {

        location.preferencesChanged(scontext.getContext(), presetIndex);
        state.preferencesChanged();
    }


        
    
    
    
    public TrackerInternals(ServiceContext sc) {
        scontext=sc;
        
        backlight = new Backlight(scontext.getContext());
        location = new LocationStack(scontext);

        storage = Storage.preset(scontext.getContext());
        storage.register(this);
        rereadPreferences();

        statusIcon = new StatusIcon(scontext);
      
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
        scontext.lock(this.getClass().getSimpleName());
        //scontext.getContext().startService(new Intent(scontext.getContext(), OneService.class));
    }

    public void unlockService() {
        scontext.free(this.getClass().getSimpleName());

        /*try {
            scontext.getService().stopSelf();
        } catch (ServiceNotUpError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }    
    
    
    
 
    
    public  TrackLogger createLogger() throws IOException {
        return new TrackLogger(scontext.getContext(),new SolidPreset(scontext.getContext()).getIndex());
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
