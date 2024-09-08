package ch.bailu.aat.services.sensor.list

import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.services.sensor.SensorInterface
import ch.bailu.aat_lib.gpx.information.GpxInformation

class SensorListItem(
    private val context: Context,
    val address: String,
    override var name: String,
    initialState: Int

) : SensorItemState(initialState), SensorInterface {
    private var sensor: SensorInterface? = null

    val isBluetoothDevice: Boolean
        get() = address.matches(BLUETOOTH_ADDRESS)

    fun lock(s: SensorInterface?): Boolean {
        return if (s == null) {
            false
        } else if (!isLocked || sensor === s) {
            sensor = s
            true
        } else {
            false
        }
    }

    fun unlock(s: SensorInterface): Boolean {
        if (isLocked(s)) {
            sensor = null
            return true
        }
        return false
    }

    private val isLocked: Boolean
        get() = sensor != null

    fun isLocked(s: SensorInterface): Boolean {
        return sensor === s
    }

    override fun getInformation(iid: Int): GpxInformation? {
        return if (isLocked) sensor!!.getInformation(iid) else null
    }

    override fun toString(): String {
        val sensorType = sensorTypeDescription
        val sensorState = getSensorStateDescription(context)
        val sensorName = name
        return "$sensorType $sensorName\n$sensorState"
    }

    private val sensorTypeDescription: String
        get() = if (isBluetoothDevice) {
            context.getString(R.string.sensor_type_bluetooth)
        } else {
            context.getString(R.string.sensor_type_internal)
        }

    override fun close() {
        if (isLocked) sensor!!.close()
    }

    fun setEnabled(enabled: Boolean) {
        if (enabled) {
            setState(ENABLED)
        } else {
            if (isLocked) {
                sensor!!.close()
            }
            setState(SUPPORTED)
        }
    }

    companion object {
        private val BLUETOOTH_ADDRESS = Regex("^([0-9A-F]{2}[:]){5}([0-9A-F]{2})$")
    }
}
