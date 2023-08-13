package ch.bailu.aat.services.sensor.list

import android.content.Context
import ch.bailu.aat.R

open class SensorItemState(var state: Int) {
    fun setState(nextState: Int): Boolean {
        if (isNextStateValid(nextState)) {
            state = nextState
            return true
        }
        return false
    }

    private fun isNextStateValid(nextState: Int): Boolean {
        if (state == UNSCANNED) {
            return nextState == SCANNING
        } else if (state == SCANNING) {
            return nextState == SUPPORTED || nextState == UNSUPPORTED
        } else if (state == SUPPORTED) {
            return nextState == ENABLED
        } else if (state == ENABLED) {
            return nextState == SUPPORTED || nextState == CONNECTING
        } else if (state == CONNECTING) {
            return nextState == CONNECTED || nextState == ENABLED || nextState == SUPPORTED
        } else if (state == CONNECTED) {
            return nextState == ENABLED || nextState == SUPPORTED
        } else if (state == UNSUPPORTED) {
            return false
        }
        return false
    }

    val isSupported: Boolean
        get() = state == SUPPORTED || isEnabled
    val isEnabled: Boolean
        get() = state == ENABLED || state == CONNECTING || state == CONNECTED
    val isConnected: Boolean
        get() = state == CONNECTED
    val isConnecting: Boolean
        get() = state == CONNECTING
    val isOpen: Boolean
        get() = state == CONNECTING || state == SCANNING
    val isUnscannedOrScanning: Boolean
        get() = state == UNSCANNED || state == SCANNING
    val isScanning: Boolean
        get() = state == SCANNING

    fun getSensorStateDescription(c: Context): String {
        return c.getString(STATE_DESCRIPTION[state])
    }

    companion object {
        const val UNSCANNED = 0
        const val SCANNING = 1
        const val SUPPORTED = 2
        const val ENABLED = 3
        const val CONNECTING = 4
        const val CONNECTED = 5
        const val UNSUPPORTED = 6

        // in future decouple
        // configuration, discovery: unscanned, supported, enabled, unsupported
        // from real time status : scanning, connecting, connected, disconnected trying to reconnect?
        private val STATE_DESCRIPTION = intArrayOf(
            R.string.sensor_state_unscanned,
            R.string.sensor_state_scanning,
            R.string.sensor_state_supported,
            R.string.sensor_state_not_connected,
            R.string.sensor_state_connecting,
            R.string.sensor_state_connected,
            R.string.sensor_state_not_supported
        )
    }
}
