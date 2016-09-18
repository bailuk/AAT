package ch.bailu.aat.services.tracker.location;

import java.io.Closeable;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidGPSLock;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;

public class ServiceLocker implements Closeable, OnSharedPreferenceChangeListener{

    private final SolidGPSLock slock;
    private final ServiceContext scontext;
    private int gpsStatus=GpxInformation.ID.STATE_OFF;
    
    
    public ServiceLocker(ServiceContext sc) {
        scontext=sc;
        final Storage storage = Storage.global(sc.getContext());
        slock=new SolidGPSLock(storage);
        
        slock.register(this);
    }


    @Override
    public void close()  {
        free();
        slock.unregister(this);
    }

    

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (slock.hasKey(key)) autoLock();
        
    }

    
    public void autoLock(int state) {
        gpsStatus=state;
        autoLock();
    }
    
    
    public void free() {
        autoLock(GpxInformation.ID.STATE_OFF);
    }
    
    
    private void autoLock() {
        if (slock.isEnabled() && 
                (gpsStatus == GpxInformation.ID.STATE_ON || gpsStatus== GpxInformation.ID.STATE_WAIT)) {
            scontext.lock(slock.getKey());
            
        } else {
            scontext.free(slock.getKey());
            
        }
    }
}
