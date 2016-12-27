package ch.bailu.aat.services.tracker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.SolidBacklight;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class TrackerInternals 
implements OnSharedPreferenceChangeListener, Closeable {


    
    public final ServiceContext scontext;
    public State state=null;

    public Logger logger;

    public final StatusIcon statusIcon;
    public final Backlight backlight;
    
    
    private final Storage storage;
    public SolidAutopause sautopause;
    public SolidBacklight sbacklight;
        
    public int presetIndex;


    public TrackerInternals(ServiceContext sc) {
        scontext=sc;

        backlight = new Backlight(scontext.getContext());

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
    }


    public void rereadPreferences() {
        presetIndex = new SolidPreset(scontext.getContext()).getIndex();
        sbacklight = new SolidBacklight(scontext.getContext(), presetIndex);
        sautopause = new SolidAutopause(scontext.getContext(), presetIndex);
    }

        
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        state.preferencesChanged();
    }


        
    
    
    

    
    public void lockService() {
        scontext.lock(this.getClass().getSimpleName());
    }

    public void unlockService() {
        scontext.free(this.getClass().getSimpleName());
    }
    
    

 
    
    public  TrackLogger createLogger() throws IOException {
        return new TrackLogger(scontext.getContext(),new SolidPreset(scontext.getContext()).getIndex());
    }
    
    
 
    
    public boolean isReadyForAutoPause() {
        return ((
                scontext.getLocationService().isMissingUpdates() ||
                scontext.getLocationService().isAutopaused())    &&
                sautopause.isEnabled());
    }

    
    public void emergencyOff(Exception e) {
        AppLog.e(scontext.getContext(), this,e);
        logger.close();
        logger = Logger.createNullLogger();
        state = new OffState(this);
    }


    @Override
    public void close() {
        logger.close();

        storage.unregister(this);            
    }


}
