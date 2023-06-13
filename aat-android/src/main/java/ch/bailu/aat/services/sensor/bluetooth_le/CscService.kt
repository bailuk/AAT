package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import ch.bailu.aat.R
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.Connector
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.CadenceSpeedAttributes
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes

class CscService(c: ServiceContext) : CscServiceID(), ServiceInterface {
    /**
     *
     * RPM BBB BCP-66 SmartCadence RPM Sensor
     *
     *
     * CSC (Cycling Speed And Cadence)
     * [...](https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.cycling_speed_and_cadence.xml)
     * [...](https://developer.polar.com/wiki/Cycling_Speed_%26_Cadence)
     */
    private var location = CadenceSpeedAttributes.SENSOR_LOCATION[0]
    private var isSpeedSensor = false
    private var isCadenceSensor = false
    private val cadence = Revolution()
    private val speed = Revolution()
    private val wheelCircumference: WheelCircumference
    private var information = GpxInformation.NULL
    override var isValid = false
        private set
    private val connectorSpeed: Connector
    private val connectorCadence: Connector
    private val broadcasterSpeed: Broadcaster
    private val broadcasterCadence: Broadcaster
    private val nameSpeed: String
    private val nameCadence: String

    init {
        wheelCircumference = WheelCircumference(c, speed)
        connectorCadence = Connector(c.context, InfoID.CADENCE_SENSOR)
        connectorSpeed = Connector(c.context, InfoID.SPEED_SENSOR)
        broadcasterCadence = Broadcaster(c.context, InfoID.CADENCE_SENSOR)
        broadcasterSpeed = Broadcaster(c.context, InfoID.SPEED_SENSOR)
        nameSpeed = c.context.getString(R.string.sensor_speed)
        nameCadence = c.context.getString(R.string.sensor_cadence)
    }

    override fun changed(c: BluetoothGattCharacteristic) {
        if (CSC_SERVICE == c.service.uuid) {
            if (CSC_MEASUREMENT == c.uuid) {
                readCscMeasurement(c, c.value)
            }
        }
    }

    override fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean {
        var disc = false
        if (CSC_SERVICE == c.service.uuid) {
            isValid = true
            disc = true
            if (CSC_FEATURE == c.uuid) {
                execute.read(c)
            } else if (CSC_SENSOR_LOCATION == c.uuid) {
                execute.read(c)
            } else if (CSC_MEASUREMENT == c.uuid) {
                execute.notify(c)
            }
        }
        return disc
    }

    override fun read(c: BluetoothGattCharacteristic) {
        if (CSC_SERVICE == c.service.uuid) {
            if (CSC_FEATURE == c.uuid) {
                readCscFeature(c.value)
            } else if (CSC_SENSOR_LOCATION == c.uuid) {
                readCscSensorLocation(c.value)
            }
        }
    }

    private fun readCscSensorLocation(v: ByteArray) {
        if (v.isNotEmpty() && v[0] < CadenceSpeedAttributes.SENSOR_LOCATION.size) {
            location = CadenceSpeedAttributes.SENSOR_LOCATION[v[0].toInt()]
        }
    }

    private fun readCscMeasurement(c: BluetoothGattCharacteristic, value: ByteArray) {
        information = Information(Attributes(this, c, value))
        connectorSpeed.connect(isSpeedSensor)
        connectorCadence.connect(isCadenceSensor)
    }

    private fun readCscFeature(v: ByteArray) {
        if (v.size > 0) {
            val b = v[0]
            isCadenceSensor = isBitSet(b, BIT_CADENCE)
            isSpeedSensor = isBitSet(b, BIT_SPEED)
        }
    }

    override fun toString(): String {
        var result = ""
        if (isValid) {
            if (isSpeedSensor && isCadenceSensor) {
                result = "$nameSpeed & $nameCadence"
            } else if (isSpeedSensor) {
                result = nameSpeed
            } else if (isCadenceSensor) {
                result = nameCadence
            }
        }
        return result
    }

    override fun close() {
        connectorSpeed.close()
        connectorCadence.close()
        wheelCircumference.close()
    }

    private class Attributes(parent: CscService, c: BluetoothGattCharacteristic, v: ByteArray) :
        CadenceSpeedAttributes(parent.location, parent.isCadenceSensor, parent.isSpeedSensor) {
        var speedSI = 0f
            private set

        init {
            var offset = 0
            val data = v[offset]
            offset += 1
            val haveCadence = isBitSet(data, BIT_CADENCE)
            val haveSpeed = isBitSet(data, BIT_SPEED)
            if (haveSpeed) {
                val revolutions =
                    c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, offset).toLong()
                offset += 4
                val time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                offset += 2
                parent.speed.addUINT32(time, revolutions)
                broadcastSpeed(
                    parent.broadcasterSpeed, parent.speed,
                    parent.wheelCircumference
                )
            }
            if (haveCadence) {
                val revolutions = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                offset += 2
                val time = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                parent.cadence.add(time, revolutions)
                broadcastCadence(parent.broadcasterCadence, parent.cadence.rpm())
            }
        }

        private fun broadcastSpeed(
            broadcasterSpeed: Broadcaster,
            speed: Revolution,
            wheelCircumference: WheelCircumference
        ) {
            if (speed.rpm() != 0 || broadcasterSpeed.timeout()) {
                circumferenceSI = wheelCircumference.circumferenceSI
                if (circumferenceSI > 0f) {
                    speedSI = speed.getSpeedSI(circumferenceSI)
                }
                circumferenceDebugString = wheelCircumference.debugString
                broadcasterSpeed.broadcast()
            }
        }

        private fun broadcastCadence(broadcasterCadence: Broadcaster, rpm: Int) {
            if (rpm != 0 || broadcasterCadence.timeout()) {
                cadence_rpm = rpm
                cadence_rpm_average = rpm
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

    override fun getInformation(iid: Int): GpxInformation? {
        if (isSpeedSensor && iid == InfoID.SPEED_SENSOR) return information else if (isCadenceSensor && iid == InfoID.CADENCE_SENSOR) {
            return information
        }
        return null
    }
}
