package ch.bailu.aat.services.cache;


import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import java.io.Closeable;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.Broadcaster;
import ch.bailu.aat_lib.service.cache.ObjBroadcastReceiver;

public final class ObjectBroadcaster implements Closeable {

    private final static int INITIAL_CAPACITY=200;

    private final AppContext appContext;

    private final SparseArray<ObjBroadcastReceiver> table = new SparseArray<>(INITIAL_CAPACITY);


    public ObjectBroadcaster(AppContext sc) {
        appContext = sc;

        appContext.getBroadcaster().register(onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        appContext.getBroadcaster().register(onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);



    }


    public synchronized void put(ObjBroadcastReceiver b) {
        table.put(b.toString().hashCode(), b);
    }


    public synchronized void delete(ObjBroadcastReceiver b) {
        delete(b.toString());
    }


    public synchronized void delete(String id) {
        table.delete(id.hashCode());
    }


    @Override
    public void close() {
        appContext.getBroadcaster().unregister(onFileDownloaded);
        appContext.getBroadcaster().unregister(onFileChanged);

    }



    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Object... objs) {
            sendOnChanged(BroadcastData.getFile(objs));
        }
    };

    private final BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {

        @Override
        public void onReceive(Object... objs) {
            sendOnDownloaded(BroadcastData.getFile(objs),BroadcastData.getUrl(objs));

        }
    };


    private synchronized void sendOnChanged(String id) {
        for (int i=0; i<table.size(); i++) {
            table.valueAt(i).onChanged(id, appContext);
        }
    }

    private synchronized void sendOnDownloaded(String id, String uri) {
        for (int i=0; i<table.size(); i++) {
            table.valueAt(i).onDownloaded(id, uri, appContext);
        }
    }

}
