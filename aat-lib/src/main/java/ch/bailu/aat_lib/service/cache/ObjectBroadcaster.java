package ch.bailu.aat_lib.service.cache;


import java.io.Closeable;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.util.IndexedMap;

public final class ObjectBroadcaster implements Closeable {

    private final AppContext appContext;

    private final IndexedMap<String, ObjBroadcastReceiver> table = new IndexedMap<>();

    private final BroadcastReceiver onFileChanged = args -> sendOnChanged(BroadcastData.getFile(args));
    private final BroadcastReceiver onFileDownloaded = args -> sendOnDownloaded(BroadcastData.getFile(args),BroadcastData.getUrl(args));

    public ObjectBroadcaster(AppContext sc) {
        appContext = sc;

        appContext.getBroadcaster().register(AppBroadcaster.FILE_CHANGED_INCACHE, onFileChanged);
        appContext.getBroadcaster().register(AppBroadcaster.FILE_CHANGED_ONDISK, onFileDownloaded);
    }

    public synchronized void put(ObjBroadcastReceiver b) {
        table.put(b.toString(), b);
    }

    public synchronized void delete(ObjBroadcastReceiver b) {
        delete(b.toString());
    }

    public synchronized void delete(String id) {
        table.remove(id);
    }

    @Override
    public void close() {
        appContext.getBroadcaster().unregister(onFileDownloaded);
        appContext.getBroadcaster().unregister(onFileChanged);

    }

    private synchronized void sendOnChanged(String id) {
        for (int i=0; i<table.size(); i++) {
            table.getValueAt(i).onChanged(id, appContext);
        }
    }

    private synchronized void sendOnDownloaded(String id, String uri) {
        for (int i=0; i<table.size(); i++) {
            table.getValueAt(i).onDownloaded(id, uri, appContext);
        }
    }
}
