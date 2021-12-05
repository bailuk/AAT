package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import java.util.ArrayList
import java.util.HashMap

class GtkBroadcaster : Broadcaster {
    private val signals: MutableMap<String, ArrayList<BroadcastReceiver>> = HashMap()

    override fun broadcast(signal: String, vararg objects: Any) {
        val observers = signals[signal]

        if (observers != null) {
            val args = toStringArgs(objects)
            for (observer in observers) {
                observer.onReceive(*args)
            }
        }
    }

    private fun toStringArgs(vararg objects: Any): Array<String> {
        val result : ArrayList<String> = ArrayList<String>(objects.size)

        for (obj in objects) {
            result.add(obj.toString())
        }
        return result.toTypedArray()
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
