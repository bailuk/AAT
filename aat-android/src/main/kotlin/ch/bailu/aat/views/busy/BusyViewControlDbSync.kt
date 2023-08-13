package ch.bailu.aat.views.busy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import java.io.Closeable

class BusyViewControlDbSync(parent: ViewGroup) : BusyViewControlIID(parent), Closeable {
    private val context: Context = parent.context

    private val onSyncStart: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            startWaiting()
        }
    }
    private val onSyncChanged: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            startWaiting()
        }
    }
    private val onSyncDone: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            stopWaiting()
        }
    }

    init {
        AndroidBroadcaster.register(context, onSyncStart, AppBroadcaster.DBSYNC_START)
        AndroidBroadcaster.register(context, onSyncDone, AppBroadcaster.DBSYNC_DONE)
        AndroidBroadcaster.register(context, onSyncChanged, AppBroadcaster.DB_SYNC_CHANGED)
    }

    override fun close() {
        context.unregisterReceiver(onSyncChanged)
        context.unregisterReceiver(onSyncDone)
        context.unregisterReceiver(onSyncStart)
    }
}
