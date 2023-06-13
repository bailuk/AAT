package ch.bailu.aat.services.sensor.list

import android.provider.BaseColumns

object SensorListDbContract : BaseColumns {
    const val TABLE_NAME = "EnabledSensors"
    const val COLUMN_NAME = "name"
    const val COLUMN_ADDRESS = "address"
}
