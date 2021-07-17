package ch.bailu.aat.services.location;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.gpx.StateID;

public final class ServiceLocker implements Closeable, OnSharedPreferenceChangeListener{

    //private final SolidGPSLock slock;
    //private final ServiceContext scontext;
    //private int gpsStatus= StateID.OFF;


    public ServiceLocker(ServiceContext sc) {
        //scontext=sc;
        //final Storage storage = Storage.global(sc.getContext());
        //slock=new SolidGPSLock(storage);

        //slock.register(this);
    }


    @Override
    public void close()  {
        free();
        //slock.unregister(this);
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        //if (slock.hasKey(key)) autoLock();

    }


    public void autoLock(int state) {
        //gpsStatus=state;
        autoLock();
    }


    public void free() {
        autoLock(StateID.OFF);
    }


    private void autoLock() {
        //if (slock.isEnabled() &&
        //        (gpsStatus == StateID.ON || gpsStatus== StateID.WAIT)) {
        //    scontext.lock(slock.getKey());

        //} else {
           // scontext.free(slock.getKey());

        //}
    }
}
