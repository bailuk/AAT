package ch.bailu.aat.dispatcher

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import ch.bailu.aat.util.AppIntent
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.foc.Foc

class AndroidBroadcaster(private val context: Context) : Broadcaster {
    private val observers: MutableMap<BroadcastReceiver, android.content.BroadcastReceiver> =
        HashMap(5)

    override fun broadcast(action: String, vararg args: String) {
        context.sendBroadcast(AppIntent.toIntent(action, *args))
    }

    override fun register(broadcastReceiver: BroadcastReceiver, action: String) {
        if (!observers.containsKey(broadcastReceiver)) {
            val receiver = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    broadcastReceiver.onReceive(*AppIntent.toArgs(intent))
                }
            }

            observers[broadcastReceiver] = receiver
            register(context, receiver, action)

        } else {
            AppLog.e(this, "Observer was already registered.")
        }
    }

    override fun unregister(broadcastReceiver: BroadcastReceiver) {
        val receiver = observers.remove(broadcastReceiver)
        if (receiver != null) {
            context.unregisterReceiver(receiver)
        } else {
            AppLog.e(this, "Observer was not registered.")
        }
    }


    companion object {
        fun broadcast(context: Context, action: String) {
            val intent = Intent()
            intent.action = action
            context.sendBroadcast(intent)
        }

        @JvmStatic
        fun register(context: Context, receiver: android.content.BroadcastReceiver, action: String) {
            val filter = IntentFilter(action)
            context.registerReceiver(receiver, filter)
        }

        fun broadcast(context: Context, action: String, file: Foc, url: String) {
            val intent = AppIntent.toIntent(action, file.path, url)
            context.sendBroadcast(intent)
        }
    }
}
