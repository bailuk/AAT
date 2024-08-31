package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.Connector
import ch.bailu.aat_lib.gpx.information.GpxInformation
import ch.bailu.aat_lib.gpx.information.InfoID
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes
import ch.bailu.aat_lib.gpx.attributes.PowerAttributes

class CyclingPower(c: ServiceContext) : CyclingPowerID(), ServiceInterface {
    private var location = CadenceSpeedAttributes.SENSOR_LOCATION[0]
    private var isSpeedSensor = false
    private var isCadenceSensor = false
    private val cadence = Revolution()
    private val speed = Revolution()
    private val wheelCircumference: WheelCircumference
    private var information = GpxInformation.NULL

    override var isValid = false
        private set
    private val connectorPower: Connector
    private val connectorSpeed: Connector
    private val connectorCadence: Connector
    private val broadcasterPower: Broadcaster
    private val broadcasterSpeed: Broadcaster
    private val broadcasterCadence: Broadcaster
    private val namePower: String
    private val nameSpeed: String
    private val nameCadence: String

    init {
        wheelCircumference = WheelCircumference(c, speed)
        connectorPower = Connector(c.getContext(), InfoID.POWER_SENSOR)
        connectorCadence = Connector(c.getContext(), InfoID.CADENCE_SENSOR)
        connectorSpeed = Connector(c.getContext(), InfoID.SPEED_SENSOR)
        broadcasterPower = Broadcaster(c.getContext(), InfoID.POWER_SENSOR)
        broadcasterCadence = Broadcaster(c.getContext(), InfoID.CADENCE_SENSOR)
        broadcasterSpeed = Broadcaster(c.getContext(), InfoID.SPEED_SENSOR)
        namePower = c.getContext().getString(R.string.sensor_power)
        nameSpeed = c.getContext().getString(R.string.sensor_speed)
        nameCadence = c.getContext().getString(R.string.sensor_cadence)
    }

    override fun close() {
        connectorPower.close()
        connectorSpeed.close()
        connectorCadence.close()
        wheelCircumference.close()
    }

    override fun changed(c: BluetoothGattCharacteristic) {
        if (CYCLING_POWER_SERVICE == c.service.uuid) {
            if (CYCLING_POWER_MEASUREMENT == c.uuid) {
                information = Information(Attributes(c))
                connectorPower.connect(true)
                connectorSpeed.connect(isSpeedSensor)
                connectorCadence.connect(isCadenceSensor)
            }
        }
    }

    override fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean {
        var disc = false
        if (CYCLING_POWER_SERVICE == c.service.uuid) {
            isValid = true
            disc = true
            if (CYCLING_POWER_FEATURE == c.uuid) {
                execute.read(c)
            } else if (SENSOR_LOCATION == c.uuid) {
                execute.read(c)
            } else if (CYCLING_POWER_MEASUREMENT == c.uuid) {
                execute.notify(c)
            }
        }
        return disc
    }

    @Suppress("DEPRECATION")
    override fun read(c: BluetoothGattCharacteristic) {
        if (CYCLING_POWER_SERVICE == c.service.uuid) {
            if (CYCLING_POWER_FEATURE == c.uuid) {
                val flags = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0)
                isSpeedSensor = flags != null && isFlagSet(flags, FEATURE_BIT_WHEEL_REVOLUTIONS)
                isCadenceSensor = flags != null && isFlagSet(flags, FEATURE_BIT_CRANK_REVOLUTIONS)
            } else if (SENSOR_LOCATION == c.uuid) {
                val i = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0)
                if (i != null && i < CadenceSpeedAttributes.SENSOR_LOCATION.size) location =
                    CadenceSpeedAttributes.SENSOR_LOCATION[i]
            }
        }
    }

    override fun getInformation(iid: Int): GpxInformation? {
        if (iid == InfoID.POWER_SENSOR) return information
        if (isSpeedSensor && iid == InfoID.SPEED_SENSOR) return information
        return if (isCadenceSensor && iid == InfoID.CADENCE_SENSOR) information else null
    }

    override fun toString(): String {
        var result = ""
        if (isValid) {
            result = namePower
            if (isSpeedSensor) result += " & $nameSpeed"
            if (isCadenceSensor) result += " & $nameCadence"
        }
        return result
    }

    @Suppress("DEPRECATION")
    private inner class Attributes(c: BluetoothGattCharacteristic) : PowerAttributes(
        location,
        isCadenceSensor,
        isSpeedSensor
    ) {
        var speedSI = 0f
            private set

        init {
            var offset = 0
            val flags = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
            offset += 2
            val instantaneousPower =
                c.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT16, offset)
            offset += 2
            broadcastPower(instantaneousPower)

            if (isFlagSet(flags, BIT_PEDAL_POWER_BALANCE)) {
                ++offset
            }

            if (isFlagSet(flags, BIT_ACCUMULATED_TORQUE)) {
                offset += 2
            }

            if (isFlagSet(flags, BIT_WHEEL_REVOLUTION_DATA)) {
                val revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset)
                offset += 4
                val time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                offset += 2
                if (revolutions != null && time != null) {
                    speed.addUINT32(time, revolutions.toLong())
                    broadcastSpeed(speed.rpm())
                }
            }

            if (isFlagSet(flags, BIT_CRANK_REVOLUTION_DATA)) {
                val revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                offset += 2
                val time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                // offset += 2;
                cadence.add(time, revolutions)
                broadcastCadence(cadence.rpm())
            }

            // TODO ...
        }

        private fun broadcastPower(_power: Int) {
            if (_power != 0 || broadcasterPower.timeout()) {
                power = _power
                broadcasterPower.broadcast()
            }
        }

        private fun broadcastSpeed(rpm: Int) {
            if (rpm != 0 || broadcasterSpeed.timeout()) {
                circumferenceSI = wheelCircumference.circumferenceSI
                if (circumferenceSI > 0f) {
                    speedSI = speed.getSpeedSI(circumferenceSI)
                }
                circumferenceDebugString = wheelCircumference.debugString
                broadcasterSpeed.broadcast()
            }
        }

        private fun broadcastCadence(rpm: Int) {
            if (rpm != 0 || broadcasterCadence.timeout()) {
                cadenceRpm = rpm
                cadenceRpmAverage = rpm
                broadcasterCadence.broadcast()
            }
        }
    }

    private class Information(private val attributes: Attributes) : GpxInformation() {
        private val timeStamp = System.currentTimeMillis()
        override fun getAttributes(): GpxAttributes {
            return attributes
        }

        override fun getTimeStamp(): Long {
            return timeStamp
        }

        override fun getSpeed(): Float {
            return attributes.speedSI
        }
    }

    companion object {
        private fun isFlagSet(flags: Int, bit: Int): Boolean {
            return flags and (1 shl bit) != 0
        }
    }
}
