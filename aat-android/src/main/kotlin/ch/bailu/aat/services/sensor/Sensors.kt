package ch.bailu.aat.services.sensor

import android.content.Context
import android.os.Build
import ch.bailu.aat.services.ServiceContext
import ch.bailu.aat.services.sensor.bluetooth_le.BleSensors
import ch.bailu.aat.services.sensor.internal.InternalSensorsSDK23
import ch.bailu.aat.services.sensor.list.SensorList
import ch.bailu.aat_lib.gpx.information.GpxInformation
import java.io.Closeable

open class Sensors : Closeable {
    open fun updateConnections() {}
    open fun scan() {}
    val information: GpxInformation
        get() = GpxInformation.NULL

    override fun close() {}

    companion object {
        fun factoryBle(sc: ServiceContext, sensorList: SensorList): Sensors {
            return BleSensors(sc, sensorList)
        }

        fun factoryInternal(c: Context, sensorList: SensorList): Sensors {
            return if (Build.VERSION.SDK_INT >= 23) {
                InternalSensorsSDK23(c, sensorList)
            } else {
                Sensors()
            }
        }
    }
}
