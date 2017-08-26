package ch.bailu.aat.services.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ch.bailu.aat.preferences.SolidCacheSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.util.AppBroadcaster;



public class CacheService extends VirtualService implements SharedPreferences.OnSharedPreferenceChangeListener {



    public final ObjectTable table=new ObjectTable();
    public final ObjectBroadcaster broadcaster;

    public final ServiceContext scontext;

    private final SolidCacheSize slimit;


    public CacheService(ServiceContext sc) {
        super(sc);

        scontext = sc;
        broadcaster = new ObjectBroadcaster(getSContext());

        slimit = new SolidCacheSize(sc.getContext());
        slimit.register(this);

        table.limit(this, slimit.getValueAsLong());

        AppBroadcaster.register(getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    public void onLowMemory() {
        table.limit(this, ObjectTable.MIN_SIZE);
        slimit.setIndex(1);
    }

    public ObjectHandle getObject(String id, ObjectHandle.Factory factory) {

        return table.getHandle(id, factory, this);
    }

    public ObjectHandle getObject(String id) {
        return table.getHandle(id, getSContext());
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        table.appendStatusText(builder);
    }


    @Override
    public void close() {
        getContext().unregisterReceiver(onFileProcessed);

        slimit.unregister(this);
        table.logLocked();
        broadcaster.close();
        table.close(this);
    }


    public void addToBroadcaster(ObjectBroadcastReceiver b) {
        broadcaster.put(b);
    }

    private final BroadcastReceiver onFileProcessed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            table.onObjectChanged(intent, CacheService.this);
        }
    };

    public void log() {
        table.log();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (slimit.hasKey(s)) {
            table.limit(this, slimit.getValueAsLong());
        }
    }
}

