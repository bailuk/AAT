package ch.bailu.aat.services.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.preferences.system.SolidCacheSize;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjBroadcastReceiver;
import ch.bailu.aat_lib.util.MemSize;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.util.WithStatusText;


public final class CacheService extends VirtualService implements CacheServiceInterface, OnPreferencesChanged, WithStatusText {



    public final ObjectTable table=new ObjectTable();
    public final ObjectBroadcaster broadcaster;

    public final ServiceContext scontext;

    private final SolidCacheSize slimit;

    public CacheService(ServiceContext sc) {
        scontext = sc;
        broadcaster = new ObjectBroadcaster(sc);

        slimit = new SolidCacheSize(new Storage(sc.getContext()));
        slimit.register(this);

        table.limit(this, slimit.getValueAsLong());

        OldAppBroadcaster.register(sc.getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    public void onLowMemory() {
        table.limit(this, MemSize.MB);
        slimit.setIndex(1);
    }

    public Obj getObject(String id, Obj.Factory factory) {

        return table.getHandle(id, factory, this);
    }

    public Obj getObject(String id) {
        return table.getHandle(id, scontext);
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        table.appendStatusText(builder);
    }


    public void close() {
        scontext.getContext().unregisterReceiver(onFileProcessed);

        slimit.unregister(this);
        table.logLocked();
        broadcaster.close();
        table.close(this);
    }


    public void addToBroadcaster(ObjBroadcastReceiver b) {
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
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (slimit.hasKey(key)) {
            table.limit(this, slimit.getValueAsLong());
        }
    }
}

