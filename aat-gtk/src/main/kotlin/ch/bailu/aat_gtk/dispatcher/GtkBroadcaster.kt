package ch.bailu.aat_gtk.dispatcher

import ch.bailu.aat_lib.dispatcher.BroadcastReceiver
import ch.bailu.aat_lib.dispatcher.Broadcaster
import ch.bailu.aat_lib.logger.AppLog
import ch.bailu.gtk.Callback
import ch.bailu.gtk.GTK
import ch.bailu.gtk.glib.Glib
import java.util.*

class GtkBroadcaster : Broadcaster {
    private val signals: MutableMap<String, ArrayList<BroadcastReceiver>> = HashMap()

    override fun broadcast(signal: String, vararg args: String) {
        val observers = signals[signal]?.toTypedArray()

        if (observers != null) {
            val emitterID = Callback.EmitterID()
            Glib.idleAdd({
                for (observer in observers) {
                    observer.onReceive(*args)
                }
                Callback.remove(emitterID)
                emitterID.destroy()
                GTK.FALSE
            }, emitterID)
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
