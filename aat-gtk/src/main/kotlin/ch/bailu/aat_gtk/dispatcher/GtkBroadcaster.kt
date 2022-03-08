package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.GTK
import ch.bailu.gtk.Refs
import ch.bailu.gtk.glib.Glib
import java.util.*

class GtkBroadcaster : Broadcaster {
    private val signals: MutableMap<String, ArrayList<BroadcastReceiver>> = HashMap()

    private var linkedList = LinkedList<Glib.OnSourceFunc>()

    override fun broadcast(signal: String, vararg args: String) {
        val observers = signals[signal]?.toTypedArray()

        if (observers != null) {
            val onSourceFunc = Glib.OnSourceFunc {
                for (observer in observers) {
                    observer.onReceive(*args)
                }
                Refs.remove(linkedList.removeLast())
                GTK.FALSE
            }
            linkedList.addFirst(onSourceFunc)
            if (linkedList.size > 5) {
                AppLog.w(this, "Stack size: ${linkedList.size}")
            }

            Glib.idleAdd(onSourceFunc, null)
        }
    }

    override fun register(observer: BroadcastReceiver, signal: String) {
        unregister(observer)
        signals.putIfAbsent(signal, ArrayList())
        signals[signal]?.add(observer)
    }

    override fun unregister(onLocation: BroadcastReceiver) {
        for (observers in signals.values) {
            observers.remove(onLocation)
        }
    }
}
