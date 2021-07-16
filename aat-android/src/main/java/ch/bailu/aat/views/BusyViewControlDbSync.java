package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import java.io.Closeable;

import ch.bailu.aat.util.AppBroadcaster;

public class BusyViewControlDbSync extends BusyViewControlIID implements Closeable {
    private final Context context;

    public BusyViewControlDbSync(ViewGroup parent) {
        super(parent);
        context = parent.getContext();
        AppBroadcaster.register(context, onSyncStart,   AppBroadcaster.DBSYNC_START);
        AppBroadcaster.register(context, onSyncDone,    AppBroadcaster.DBSYNC_DONE);
        AppBroadcaster.register(context, onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);

    }

    private final BroadcastReceiver
            onSyncStart = new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            startWaiting();

        }

    },

    onSyncChanged  =new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            startWaiting();

        }

    },

    onSyncDone = new BroadcastReceiver () {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopWaiting();
        }

    };






    @Override
    public void close() {
        context.unregisterReceiver(onSyncChanged);
        context.unregisterReceiver(onSyncDone);
        context.unregisterReceiver(onSyncStart);
    }
}
