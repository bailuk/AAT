package ch.bailu.aat.services.sensor.list

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class SensorListDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE " + SensorListDbContract.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY," +
                SensorListDbContract.COLUMN_NAME + " TEXT," +
                SensorListDbContract.COLUMN_ADDRESS + " TEXT)"
        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SensorListDbContract.TABLE_NAME
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "SensorList.db"
    }
}
