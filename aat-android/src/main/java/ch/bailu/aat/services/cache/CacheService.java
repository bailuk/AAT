package ch.bailu.aat.services.cache;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.system.SolidCacheSize;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjBroadcastReceiver;
import ch.bailu.aat_lib.util.MemSize;
import ch.bailu.aat_lib.util.WithStatusText;


public final class CacheService extends VirtualService implements CacheServiceInterface, OnPreferencesChanged, WithStatusText {



    public final ObjectTable table=new ObjectTable();
    public final ObjectBroadcaster broadcaster;

    public final AppContext appContext;

    private final SolidCacheSize slimit;

    public CacheService(AppContext sc) {
        appContext = sc;
        broadcaster = new ObjectBroadcaster(sc);

        slimit = new SolidCacheSize(sc.getStorage());
        slimit.register(this);

        table.limit(this, slimit.getValueAsLong());

        sc.getBroadcaster().register(onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    public void onLowMemory() {
        table.limit(this, MemSize.MB);
        slimit.setIndex(1);
    }

    public Obj getObject(String id, Obj.Factory factory) {

        return table.getHandle(id, factory, this);
    }

    public Obj getObject(String id) {
        return table.getHandle(id, appContext);
    }

    @Override
    public void appendStatusText(StringBuilder builder) {
        table.appendStatusText(builder);
    }


    public void close() {
        appContext.getBroadcaster().unregister(onFileProcessed);

        slimit.unregister(this);
        broadcaster.close();
        table.close(this);
    }


    public void addToBroadcaster(ObjBroadcastReceiver b) {
        broadcaster.put(b);
    }

    private final BroadcastReceiver onFileProcessed = new BroadcastReceiver() {
        @Override
        public void onReceive(Object... objs) {
            table.onObjectChanged(CacheService.this, objs);
        }
    };

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (slimit.hasKey(key)) {
            table.limit(this, slimit.getValueAsLong());
        }
    }
}

