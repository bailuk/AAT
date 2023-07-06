package ch.bailu.aat.services.sensor

import android.content.Context
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat_lib.dispatcher.AppBroadcaster
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.service.sensor.SensorState
import java.io.Closeable

class Connector(private val context: Context, private val iid: Int) : Closeable {
    var isConnected = false
        private set

    fun connect() {
        if (!isConnected) {
            isConnected = true
            SensorState.connect(iid)
            broadcast()
        }
    }

    fun connect(condition: Boolean) {
        if (condition) {
            connect()
        }
    }

    private fun broadcast() {
        AndroidBroadcaster.broadcast(context, changedAction)
    }

    override fun close() {
        if (isConnected) {
            isConnected = false
            SensorState.disconnect(iid)

            // just disconnected try reconnect
            AndroidBroadcaster.broadcast(context, disconnectedAction)
        }
    }

    companion object {
        private val changedAction = AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS
        private val disconnectedAction = AppBroadcaster.SENSOR_DISCONNECTED + InfoID.SENSORS
    }
}
