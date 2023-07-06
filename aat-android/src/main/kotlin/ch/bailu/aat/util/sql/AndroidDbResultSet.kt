package ch.bailu.aat.util.sql

import android.database.Cursor
import ch.bailu.aat_lib.util.sql.DbResultSet

class AndroidDbResultSet(private val cursor: Cursor) : DbResultSet {
    override fun moveToFirst(): Boolean {
        return cursor.moveToFirst()
    }

    override fun moveToNext(): Boolean {
        return cursor.moveToNext()
    }

    override fun close() {
        cursor.close()
    }

    override fun moveToPrevious(): Boolean {
        return cursor.moveToPrevious()
    }

    override fun moveToPosition(pos: Int): Boolean {
        return cursor.moveToPosition(pos)
    }

    override fun getCount(): Int {
        return cursor.count
    }

    override fun getString(column: String): String {
        val index = cursor.getColumnIndex(column)
        return if (index > -1) cursor.getString(index) else ""
    }

    override fun getLong(column: String): Long {
        val index = cursor.getColumnIndex(column)
        return if (index > -1) cursor.getLong(index) else 0
    }

    override fun getFloat(column: String): Float {
        val index = cursor.getColumnIndex(column)
        return if (index > -1) cursor.getFloat(index) else 0f
    }

    override fun getPosition(): Int {
        return cursor.position
    }

    override fun isClosed(): Boolean {
        return cursor.isClosed
    }
}
