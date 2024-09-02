package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.broadcaster.BroadcastReceiver
import ch.bailu.aat_lib.broadcaster.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.glib.Glib
import java.util.concurrent.ConcurrentLinkedQueue

class GtkBroadcaster : Broadcaster {
    private val signals: MutableMap<String, ArrayList<BroadcastReceiver>> = HashMap()
    private val broadcastQueue = ConcurrentLinkedQueue<BroadcastEntry>()
    private val onSourceFunc = Glib.OnSourceFunc { self, _ ->
        do {
            val entry = broadcastQueue.poll()
            if (entry is BroadcastEntry) {
                entry.broadcast()
            }
        } while(entry != null)

        self.unregister()
        false
    }

    @Synchronized
    override fun broadcast(action: String, vararg args: String) {
        val observers = signals[action]?.toTypedArray()

        if (observers != null) {
            if (broadcastQueue.offer(BroadcastEntry(observers, arrayOf(*args)))) {
                Glib.idleAdd(onSourceFunc, null)
            } else {
                AppLog.e(this, "Failed to queue broadcast entry")
            }
        }
    }

    @Synchronized
    override fun register(action: String, broadcastReceiver: BroadcastReceiver) {
        unregister(broadcastReceiver)
        signals.putIfAbsent(action, ArrayList())
        signals[action]?.add(broadcastReceiver)
    }

    @Synchronized
    override fun unregister(broadcastReceiver: BroadcastReceiver) {
        for (observers in signals.values) {
            observers.remove(broadcastReceiver)
        }
    }
}

class BroadcastEntry(private val observers: Array<BroadcastReceiver>, private val args: Array<String>) {
    fun broadcast() {
        for (observer in observers) {
            observer.onReceive(*args)
        }
    }
}
