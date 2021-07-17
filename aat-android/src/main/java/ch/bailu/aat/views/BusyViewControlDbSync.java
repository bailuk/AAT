package ch.bailu.aat.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;

import java.io.Closeable;

import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;

public class BusyViewControlDbSync extends BusyViewControlIID implements Closeable {
    private final Context context;

    public BusyViewControlDbSync(ViewGroup parent) {
        super(parent);
        context = parent.getContext();
        OldAppBroadcaster.register(context, onSyncStart,   AppBroadcaster.DBSYNC_START);
        OldAppBroadcaster.register(context, onSyncDone,    AppBroadcaster.DBSYNC_DONE);
        OldAppBroadcaster.register(context, onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED);

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
