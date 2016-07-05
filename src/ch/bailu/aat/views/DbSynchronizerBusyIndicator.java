package ch.bailu.aat.views;

import java.io.Closeable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;

public class DbSynchronizerBusyIndicator implements Closeable {

    private final BusyButton busy;
    
    private final BroadcastReceiver  
    onSyncStart = new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            busy.startWaiting();
            
        }

    },

    onSyncChanged  =new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            busy.startWaiting();
            
        }

    },

    onSyncDone = new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            busy.stopWaiting();
        }

    };


   
    public DbSynchronizerBusyIndicator(BusyButton b) {
        busy=b;
        

   
        AppBroadcaster.register(busy.getContext(), onSyncStart,   AppBroadcaster.DBSYNC_START);
        AppBroadcaster.register(busy.getContext(), onSyncDone,    AppBroadcaster.DBSYNC_DONE);
        AppBroadcaster.register(busy.getContext(), onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);

    }



    @Override
    public void close() {
        busy.getContext().unregisterReceiver(onSyncChanged);
        busy.getContext().unregisterReceiver(onSyncDone);
        busy.getContext().unregisterReceiver(onSyncStart);
    }
}
