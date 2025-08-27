package ch.bailu.aat_lib.util.sql

import ch.bailu.aat_lib.logger.AppLog.e

class SaveDbResultSet(private val resultSet: DbResultSet) : DbResultSet {
    override fun moveToFirst(): Boolean {
        try {
            return resultSet.moveToFirst()
        } catch (e: Exception) {
            e(this, e)
        }
        return false
    }

    override fun moveToNext(): Boolean {
        try {
            return resultSet.moveToNext()
        } catch (e: Exception) {
            e(this, e)
        }
        return false
    }

    override fun moveToPrevious(): Boolean {
        try {
            return resultSet.moveToPrevious()
        } catch (e: Exception) {
            e(this, e)
        }
        return false
    }

    override fun moveToPosition(pos: Int): Boolean {
        try {
            return resultSet.moveToPosition(pos)
        } catch (e: Exception) {
            e(this, e)
        }
        return false
    }

    override fun getPosition(): Int {
        try {
            return resultSet.getPosition()
        } catch (e: Exception) {
            e(this, e)
        }
        return 0
    }

    override fun getCount(): Int {
        try {
            return resultSet.getCount()
        } catch (e: Exception) {
            e(this, e)
        }
        return 0
    }

    override fun getString(column: String): String {
        try {
            return resultSet.getString(column)
        } catch (e: Exception) {
            e(this, e)
        }
        return ""
    }

    override fun getLong(column: String): Long {
        try {
            return resultSet.getLong(column)
        } catch (e: Exception) {
            e(this, e)
        }
        return 0L
    }

    override fun getFloat(column: String): Float {
        try {
            return resultSet.getFloat(column)
        } catch (e: Exception) {
            e(this, e)
        }
        return 0f
    }

    override fun isClosed(): Boolean {
        try {
            return resultSet.isClosed()
        } catch (e: Exception) {
            e(this, e)
        }
        return true
    }

    override fun close() {
        try {
            resultSet.close()
        } catch (e: Exception) {
            e(this, e)
        }
    }
}
