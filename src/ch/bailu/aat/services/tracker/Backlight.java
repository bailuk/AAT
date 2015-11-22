package ch.bailu.aat.services.tracker;


import ch.bailu.aat.preferences.SolidBacklight;

import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class  Backlight extends ContextWrapper {

    private WakeLock wakeLock=null;
    
    public Backlight(Context base) {
        super(base);
    }
    
    
    public void setToOff() {
        unlock();
        initWakeLock(PowerManager.PARTIAL_WAKE_LOCK);
    }
    
    public void setToPreferred(SolidBacklight sbacklight) {
        unlock();
        initWakeLock(sbacklight.getLockMode());
    }
    
    public void unlock() {
        if (haveWakeLock()) wakeLock.release();
        wakeLock=null;
    }
    
    private void initWakeLock(int mode) {
        PowerManager powerManager = (PowerManager) getSystemService(Service.POWER_SERVICE); 
        wakeLock = powerManager.newWakeLock(mode, getClass().getName());
        wakeLock.acquire();
    }

    private boolean haveWakeLock() {
        return wakeLock != null;
    }
}
