package ch.bailu.aat.services.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import java.io.Closeable;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;

public class ObjectBroadcaster implements Closeable {

    private final static int INITIAL_CAPACITY=200;

    private final ServiceContext serviceContext;

    private final SparseArray<ObjectBroadcastReceiver> table = new SparseArray<>(INITIAL_CAPACITY);


    public ObjectBroadcaster(ServiceContext sc) {
        serviceContext = sc;

        AppBroadcaster.register(sc.getContext(), onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        AppBroadcaster.register(sc.getContext(), onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);



    }


    public synchronized void put(ObjectBroadcastReceiver b) {
        table.put(b.toString().hashCode(), b);
    }


    public synchronized void delete(ObjectBroadcastReceiver b) {
        delete(b.toString());
    }


    public synchronized void delete(String id) {
        table.delete(id.hashCode());
    }


    @Override
    public void close() {
        serviceContext.getContext().unregisterReceiver(onFileDownloaded);
        serviceContext.getContext().unregisterReceiver(onFileChanged);

    }



    private final BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sendOnChanged(AppIntent.getFile(intent));

        }
    };

    private final BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            sendOnDownloaded(AppIntent.getFile(intent),AppIntent.getUrl(intent));

        }
    };


    private synchronized void sendOnChanged(String id) {
        for (int i=0; i<table.size(); i++) {
            table.valueAt(i).onChanged(id, serviceContext);
        }
    }

    private synchronized void sendOnDownloaded(String id, String uri) {
        for (int i=0; i<table.size(); i++) {
            table.valueAt(i).onDownloaded(id, uri, serviceContext);
        }
    }

}
