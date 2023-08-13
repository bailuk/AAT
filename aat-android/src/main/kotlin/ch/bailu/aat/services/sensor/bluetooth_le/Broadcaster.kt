package ch.bailu.aat.services.sensor.bluetooth_le

import android.content.Context
import ch.bailu.aat.dispatcher.AndroidBroadcaster
import ch.bailu.aat_lib.dispatcher.AppBroadcaster

class Broadcaster(private val context: Context, iid: Int) {
    companion object {
        private const val BROADCAST_TIMEOUT: Long = 2000
    }

    private val action: String = AppBroadcaster.SENSOR_CHANGED + iid
    private var lastBroadcast = 0L

    fun timeout(): Boolean {
        return System.currentTimeMillis() - lastBroadcast > BROADCAST_TIMEOUT
    }

    fun broadcast() {
        lastBroadcast = System.currentTimeMillis()
        AndroidBroadcaster.broadcast(context, action)
    }
}
