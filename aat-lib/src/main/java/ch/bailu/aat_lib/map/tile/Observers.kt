package ch.bailu.aat_lib.map.tile

import org.mapsforge.map.model.common.ObservableInterface
import org.mapsforge.map.model.common.Observer

class Observers : ObservableInterface {
    private val observers = ArrayList<Observer>(2)
    @Synchronized
    override fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    @Synchronized
    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    @Synchronized
    fun notifyChange() {
        for (o in observers) o.onChange()
    }
}
