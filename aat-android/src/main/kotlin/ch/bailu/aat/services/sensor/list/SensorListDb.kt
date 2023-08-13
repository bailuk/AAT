package ch.bailu.aat.services.sensor.list

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

object SensorListDb {
    fun write(context: Context?, list: SensorList) {
        val db = SensorListDbHelper(context).writableDatabase
        db.delete(SensorListDbContract.TABLE_NAME, null, null)
        list.forEach { item: SensorListItem -> if (item.isEnabled) write(db, item) }
        db.close()
    }

    private fun write(db: SQLiteDatabase, item: SensorListItem) {
        val values = ContentValues()
        values.put(SensorListDbContract.COLUMN_NAME, item.name)
        values.put(SensorListDbContract.COLUMN_ADDRESS, item.address)
        db.insert(SensorListDbContract.TABLE_NAME, null, values)
    }

    fun read(context: Context?, list: SensorList) {
        val db = SensorListDbHelper(context).readableDatabase
        val cursor = db.query(
            SensorListDbContract.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            val name = cursor.getString(
                cursor.getColumnIndexOrThrow(SensorListDbContract.COLUMN_NAME)
            )
            val address = cursor.getString(
                cursor.getColumnIndexOrThrow(SensorListDbContract.COLUMN_ADDRESS)
            )
            list.addEnabled(address, name)
        }
        cursor.close()
        db.close()
    }
}
