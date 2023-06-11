package ch.bailu.aat.dispatcher

import android.content.Context
import android.content.Intent
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat.util.OldAppBroadcaster
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.logger.AppLog

class AndroidBroadcaster(private val context: Context) : Broadcaster {
    private val observers: MutableMap<BroadcastReceiver, android.content.BroadcastReceiver> =
        HashMap(5)

    override fun broadcast(action: String, vararg args: String) {
        context.sendBroadcast(AppIntent.toIntent(action, args))
    }

    override fun register(observer: BroadcastReceiver, action: String) {
        if (!observers.containsKey(observer)) {
            observers[observer] = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    observer.onReceive(*AppIntent.toArgs(intent))
                }
            }
            OldAppBroadcaster.register(context, observers[observer], action)
        } else {
            AppLog.e(this, "Observer was already registered.")
        }
    }

    override fun unregister(observer: BroadcastReceiver) {
        val receiver = observers.remove(observer)
        if (receiver != null) {
            context.unregisterReceiver(receiver)
        } else {
            AppLog.e(this, "Observer was not registered.")
        }
    }
}
