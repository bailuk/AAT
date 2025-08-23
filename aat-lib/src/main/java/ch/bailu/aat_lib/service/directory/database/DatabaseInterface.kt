package ch.bailu.aat_lib.service.directory.database

import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc
import java.io.Closeable

interface DatabaseInterface : Closeable {
    fun query(selection: String): DbResultSet?

    @Throws(DbException::class)
    fun deleteEntry(file: Foc)

    override fun close() {}

    companion object {
        val NULL_DATABASE: DatabaseInterface = object : DatabaseInterface {
            override fun query(selection: String): DbResultSet? {
                return null
            }

            override fun deleteEntry(file: Foc) {}
        }
    }
}
