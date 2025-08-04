package ch.bailu.aat.services.sensor.list

import android.content.Context
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.information.StateID
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.SensorStateAttributes
import java.io.Closeable

class SensorList(private val context: Context) :  Closeable {
    private val list = ArrayList<SensorListItem>(10)

    companion object {
        private const val CHANGE_ACTION = AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS
    }

    init {
        restore()
    }

    fun add(address: String, name: String): SensorListItem {
        return add(address, name, SensorItemState.UNSCANNED)
    }

    fun addEnabled(address: String, name: String): SensorListItem {
        return add(address, name, SensorItemState.ENABLED)
    }

    private fun add(address: String, name: String, initialState: Int): SensorListItem {
        var item = find(address)
        if (item == null) {
            item = SensorListItem(context, address, name, initialState)
            list.add(item)
        }
        return item
    }

    private fun find(address: String): SensorListItem? {
        for (i in list) {
            if (i.address.equals(address, ignoreCase = true)) return i
        }
        return null
    }

    fun broadcast() {
        AndroidBroadcaster.broadcast(context, CHANGE_ACTION)
    }

    fun getInformation(iid: Int): GpxInformation? {
        if (iid == InfoID.SENSORS) return Information(list)
        for (item in list) {
            val i = item.getInformation(iid)
            if (i != null) return i
        }
        return null
    }

    override fun close() {
        save()
        closeConnections()
    }

    private fun closeConnections() {
        for (i in list) {
            i.close()
        }
    }

    private fun save() {
        SensorListDb.write(context, this)
    }

    private fun restore() {
        SensorListDb.read(context, this)
    }

    fun forEach(action: (SensorListItem) -> Unit) {
        list.forEach(action)
    }

    fun size(): Int {
        return list.size
    }

    fun get(i: Int): SensorListItem {
        return list[i]
    }

    class Information(list: Iterable<SensorListItem>) : GpxInformation() {
        private var state = StateID.OFF
        private val attributes: SensorStateAttributes

        init {
            var sensorCount = 0
            for (i in list) {
                if (i.isConnected) {
                    sensorCount++
                } else if (i.isConnecting) {
                    state = StateID.WAIT
                }
            }
            if (state != StateID.WAIT && sensorCount > 0) state = StateID.ON
            attributes = SensorStateAttributes(sensorCount)
        }

        override fun getState(): Int {
            return state
        }

        override fun getAttributes(): GpxAttributes {
            return attributes
        }
    }
}
