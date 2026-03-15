package ch.bailu.aat.services.sensor

import android.content.Context
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat_lib.broadcaster.AppBroadcaster
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.service.sensor.SensorState
import java.io.Closeable

/**
 * Manages the connected/disconnected state of a single sensor [InfoID].
 *
 * On [connect], updates global [SensorState] and broadcasts
 * [AppBroadcaster.SENSOR_CHANGED]. On [close], marks the sensor as
 * disconnected and broadcasts [AppBroadcaster.SENSOR_DISCONNECTED] to
 * trigger reconnection attempts.
 */
class Connector(private val context: Context, private val iid: Int) : Closeable {
    private var isConnected = false

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
        AndroidBroadcaster.broadcast(context, CHANGE_ACTION)
    }

    override fun close() {
        if (isConnected) {
            isConnected = false
            SensorState.disconnect(iid)

            // just disconnected try reconnect
            AndroidBroadcaster.broadcast(context, DISCONNECT_ACTION)
        }
    }

    companion object {
        private const val CHANGE_ACTION = AppBroadcaster.SENSOR_CHANGED + InfoID.SENSORS
        private const val DISCONNECT_ACTION = AppBroadcaster.SENSOR_DISCONNECTED + InfoID.SENSORS
    }
}
