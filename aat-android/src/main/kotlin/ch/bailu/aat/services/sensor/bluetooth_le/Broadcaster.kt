package ch.bailu.aat.services.sensor.bluetooth_le

import android.content.Context
import ch.bailu.aat.broadcaster.AndroidBroadcaster
import ch.bailu.aat_lib.broadcaster.AppBroadcaster

/**
 * Throttled Android-broadcast emitter for sensor updates.
 *
 * Sends an [AppBroadcaster.SENSOR_CHANGED] intent keyed by InfoID.
 * [timeout] returns true when at least [BROADCAST_TIMEOUT] ms have elapsed,
 * allowing callers to suppress zero-value broadcasts until the timeout expires.
 * Downstream [SensorSource] instances listen for these intents to push data
 * into the [Dispatcher] chain.
 */
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
