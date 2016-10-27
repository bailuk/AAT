package ch.bailu.aat.services.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;



public class CacheService extends VirtualService {

    public final ObjectTable table=new ObjectTable();
    public final ObjectBroadcaster broadcaster;

    public final ServiceContext scontext;

    public CacheService(ServiceContext sc) {
        super(sc);

        scontext = sc;
        broadcaster = new ObjectBroadcaster(getSContext());


        AppBroadcaster.register(getContext(), onFileProcessed, AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    public void onLowMemory() {
        table.onLowMemory(this);
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
        table.logLocked();
        getContext().unregisterReceiver(onFileProcessed);
        broadcaster.close();
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
}

