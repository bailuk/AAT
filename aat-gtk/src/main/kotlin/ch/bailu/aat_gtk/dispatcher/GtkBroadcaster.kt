package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.GTK
import ch.bailu.gtk.Refs
import ch.bailu.gtk.glib.Glib
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class GtkBroadcaster : Broadcaster {

    private val signals: MutableMap<String, ArrayList<BroadcastReceiver>> = HashMap()
    private val broadcastQueue = ConcurrentLinkedQueue<BroadcastEntry>()
    private val onSourceFunc = Glib.OnSourceFunc {
        do {
            val entry = broadcastQueue.poll()
            if (entry is BroadcastEntry) {
                entry.broadcast()
            }
        } while(entry != null)

        GTK.FALSE
    }

    @Synchronized
    override fun broadcast(signal: String, vararg args: String) {
        val observers = signals[signal]?.toTypedArray()

        if (observers != null) {
            if (broadcastQueue.offer(BroadcastEntry(observers, arrayOf(*args)))) {
                Glib.idleAdd(onSourceFunc, null)
            } else {
                AppLog.e(this, "Failed to queue broadcast entry")
            }
        }
    }

    @Synchronized
    override fun register(observer: BroadcastReceiver, signal: String) {
        unregister(observer)
        signals.putIfAbsent(signal, ArrayList())
        signals[signal]?.add(observer)
    }

    @Synchronized
    override fun unregister(onLocation: BroadcastReceiver) {
        for (observers in signals.values) {
            observers.remove(onLocation)
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
