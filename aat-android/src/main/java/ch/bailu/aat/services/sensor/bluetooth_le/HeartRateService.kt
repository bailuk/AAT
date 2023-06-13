package ch.bailu.aat.services.sensor.bluetooth_le

import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import ch.bailu.aat.R
import ch.bailu.aat.services.sensor.Connector
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.gpx.attributes.HeartRateAttributes
import ch.bailu.aat_lib.gpx.attributes.SensorInformation
import kotlin.math.roundToInt

class HeartRateService(c: Context) : HeartRateServiceID(), ServiceInterface {
    /**
     *
     * EC DMH30 0ADE BBB Bluepulse+ Heart Rate Sensor BCP-62DB
     * [...](https://www.bluetooth.com/specifications/gatt/viewer?attributeXmlFile=org.bluetooth.service.heart_rate.xml)
     *
     */
    private var location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[0]
    private var information: GpxInformation? = null
    override var isValid = false
        private set
    private val connector: Connector
    private val broadcaster: Broadcaster
    private val name: String

    init {
        connector = Connector(c, InfoID.HEART_RATE_SENSOR)
        broadcaster = Broadcaster(c, InfoID.HEART_RATE_SENSOR)
        name = c.getString(R.string.sensor_heart_rate)
    }

    override fun discovered(c: BluetoothGattCharacteristic, execute: Executor): Boolean {
        val sid = c.service.uuid
        val cid = c.uuid
        var disc = false
        if (HEART_RATE_SERVICE == sid) {
            isValid = true
            disc = true
            if (HEART_RATE_MEASUREMENT == cid) {
                execute.notify(c)
            } else if (BODY_SENSOR_LOCATION == cid) {
                execute.read(c)
            }
        }
        return disc
    }

    override fun read(c: BluetoothGattCharacteristic) {
        if (HEART_RATE_SERVICE == c.service.uuid) {
            if (BODY_SENSOR_LOCATION == c.uuid) {
                readBodySensorLocation(c.value)
                connector.connect(isValid)
                information = SensorInformation(HeartRateAttributes(location))
                broadcaster.broadcast()
            }
        }
    }

    override fun changed(c: BluetoothGattCharacteristic) {
        if (HEART_RATE_SERVICE == c.service.uuid) {
            if (HEART_RATE_MEASUREMENT == c.uuid) {
                readHeartRateMeasurement(c, c.value)
            }
        }
    }

    private fun readHeartRateMeasurement(c: BluetoothGattCharacteristic, value: ByteArray) {
        information = SensorInformation(Attributes(c, value))
    }

    override fun toString(): String {
        return name
    }

    private fun readBodySensorLocation(value: ByteArray) {
        if (value[0] < HeartRateAttributes.BODY_SENSOR_LOCATIONS.size) {
            location = HeartRateAttributes.BODY_SENSOR_LOCATIONS[value[0].toInt()]
        }
    }

    override fun close() {
        connector.close()
        broadcaster.broadcast()
    }

    private inner class Attributes(c: BluetoothGattCharacteristic, v: ByteArray) :
        HeartRateAttributes(
            location
        ) {
        init {
            var offset = 0
            val flags = v[offset]
            val bpmUint16 = isBitSet(flags, 0)
            val haveSensorContactStatus = isBitSet(flags, 1)
            haveSensorContact = isBitSet(flags, 2)
            val haveEnergyExpended = isBitSet(flags, 3)
            val haveRrInterval = isBitSet(flags, 4)
            offset += 1
            offset += if (bpmUint16) {
                setBpm(c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset))
                2
            } else {
                setBpm(c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset))
                1
            }
            if (haveEnergyExpended) {
                offset += 2
            }
            if (haveRrInterval) {
                rrIntervall = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT16, offset)
                if (!haveBpm() && rrIntervall > 0) {
                    setBpm((MINUTE / rrIntervall.toFloat()).roundToInt())
                }
            }
            if (haveBpm()) {
                if (!haveSensorContactStatus) haveSensorContact = true
                broadcaster.broadcast()
            } else if (haveSensorContactStatus && !haveSensorContact) {
                broadcaster.broadcast()
            } else if (broadcaster.timeout()) {
                if (!haveSensorContactStatus) haveSensorContact = false
                broadcaster.broadcast()
            }
        }
    }

    override fun getInformation(iid: Int): GpxInformation? {
        return if (iid == InfoID.HEART_RATE_SENSOR) information else null
    }
}
