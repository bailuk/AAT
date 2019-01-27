package ch.bailu.aat.services.tracker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.Closeable;
import java.io.IOException;

import ch.bailu.aat.preferences.SolidAutopause;
import ch.bailu.aat.preferences.presets.SolidPreset;
import ch.bailu.aat.preferences.presets.SolidTrackerAutopause;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;

public class TrackerInternals 
implements OnSharedPreferenceChangeListener, Closeable {


    
    public final ServiceContext scontext;
    public State state;

    public Logger logger;

    public final StatusIcon statusIcon;

    
    private final Storage storage;
    public SolidAutopause sautopause;

    public int presetIndex;


    public TrackerInternals(ServiceContext sc) {
        scontext=sc;

        storage = new Storage(scontext.getContext());
        rereadPreferences();

        statusIcon = new StatusIcon(scontext);

        try {
            logger = createLogger();
            logger.close();
        } catch (Exception e) {
            AppLog.e(sc.getContext(), e);
        }
        logger = Logger.createNullLogger();

        state = new OffState(this);
        storage.register(this);

    }


    public void rereadPreferences() {
        presetIndex = new SolidPreset(scontext.getContext()).getIndex();
        sautopause = new SolidTrackerAutopause(scontext.getContext(), presetIndex);

        scontext.getLocationService().setPresetIndex(presetIndex);
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
    
    

 
    
    public  TrackLogger createLogger() throws IOException, SecurityException, XmlPullParserException {
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
