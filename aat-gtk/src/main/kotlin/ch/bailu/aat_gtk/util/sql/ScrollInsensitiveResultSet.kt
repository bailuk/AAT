package ch.bailu.aat_gtk.util.sql

import ch.bailu.aat_lib.util.sql.DbResultSet
import java.sql.ResultSet

/**
 * https://www.baeldung.com/jdbc-resultset
 * https://stackoverflow.com/questions/192078/how-do-i-get-the-size-of-a-java-sql-resultset
 */
class ScrollInsensitiveResultSet(private val resultSet: ResultSet) : DbResultSet {

    private val size = if (resultSet.last()) {
        resultSet.row
    } else {
        0
    }

    override fun moveToFirst(): Boolean {
        return resultSet.first()
    }

    override fun moveToNext(): Boolean {
        return resultSet.next()
    }

    override fun close() {
        resultSet.close()
    }

    override fun moveToPrevious(): Boolean {
        return resultSet.previous()
    }

    override fun moveToPosition(pos: Int): Boolean {
        return resultSet.absolute(pos + 1)
    }

    override fun getCount(): Int {
        return size
    }

    override fun getString(column: String): String {
        return resultSet.getString(column)
    }

    override fun getLong(column: String): Long {
        return resultSet.getLong(column)
    }

    override fun getFloat(column: String): Float {
        return resultSet.getFloat(column)
    }

    override fun getPosition(): Int {
        return resultSet.row - 1
    }

    override fun isClosed(): Boolean {
        return resultSet.isClosed
    }
}
