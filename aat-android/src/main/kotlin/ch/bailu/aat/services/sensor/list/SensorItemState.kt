package ch.bailu.aat.services.sensor.list

import android.content.Context
import ch.bailu.aat.R

open class SensorItemState {
    var enabledState = false

    enum class SupportedState { UNKNOWN, SCANNING, YES, NO }
    var supportedState = SupportedState.UNKNOWN

    enum class ConnectionState { NO, IN_PROGRESS, YES }
    var connectionState = ConnectionState.NO

    val isSupported: Boolean
        get() = supportedState == SupportedState.YES
    val isEnabled: Boolean
        get() = enabledState
    val isConnected: Boolean
        get() = connectionState == ConnectionState.YES
    val isConnecting: Boolean
        get() = connectionState == ConnectionState.IN_PROGRESS
    val isOpen: Boolean
        get() = connectionState != ConnectionState.NO
    val shouldScan: Boolean
        get() = supportedState == SupportedState.UNKNOWN || supportedState == SupportedState.NO
    val isScanning: Boolean
        get() = supportedState == SupportedState.SCANNING

    private fun getSensorStateDescriptionId(): Int {
        return when (connectionState) {
            ConnectionState.YES -> when (supportedState) {
                                       SupportedState.SCANNING -> R.string.sensor_state_scanning
                                       SupportedState.UNKNOWN -> R.string.sensor_state_connected
                                       SupportedState.YES -> R.string.sensor_state_connected
                                       SupportedState.NO -> R.string.sensor_state_connected
                                   }
            ConnectionState.IN_PROGRESS -> when (supportedState) {
                                               SupportedState.SCANNING -> R.string.sensor_state_scanning
                                               SupportedState.UNKNOWN -> R.string.sensor_state_connecting
                                               SupportedState.YES -> R.string.sensor_state_connecting
                                               SupportedState.NO -> R.string.sensor_state_connecting
                                           }
            ConnectionState.NO -> when (supportedState) {
                                      SupportedState.SCANNING -> R.string.sensor_state_scanning
                                      SupportedState.UNKNOWN -> R.string.sensor_state_unscanned
                                      SupportedState.YES -> R.string.sensor_state_not_connected
                                      SupportedState.NO -> R.string.sensor_state_not_supported
                                  }
        }
    }

    fun getSensorStateDescription(c: Context): String {
        return c.getString(getSensorStateDescriptionId())
    }
}
