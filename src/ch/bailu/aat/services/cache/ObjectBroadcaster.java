package ch.bailu.aat.services.cache;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.cache.CacheService.SelfOn;

public class ObjectBroadcaster implements Closeable {
    
    private final static int INITIAL_CAPACITY=200;

    private final SelfOn self;

    private final SparseArray<ObjectBroadcastReceiver> table = new SparseArray<ObjectBroadcastReceiver>(INITIAL_CAPACITY);


    public ObjectBroadcaster(SelfOn s) {
        self = s;
        
        AppBroadcaster.register(self.context, onFileChanged, AppBroadcaster.FILE_CHANGED_INCACHE);
        AppBroadcaster.register(self.context, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
        
        
        
    }


    public void put(ObjectBroadcastReceiver b) {
        table.put(b.toString().hashCode(), b);
    }

    
    public void delete(ObjectBroadcastReceiver b) {
        delete(b.toString());
    }
    
    
    public void delete(String id) {
        table.delete(id.hashCode());
    }


    @Override
    public void close() {
        self.context.unregisterReceiver(onFileDownloaded);
        self.context.unregisterReceiver(onFileChanged);

    }

    private BroadcastReceiver onFileChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i=0; i<table.size(); i++) {
                table.valueAt(i).onChanged(AppBroadcaster.getFile(intent), self);
            }
        }
    };
    
    private BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i=0; i<table.size(); i++) {
                table.valueAt(i).onDownloaded(AppBroadcaster.getFile(intent),AppBroadcaster.getUrl(intent), self);
            }
        }
    };

}
