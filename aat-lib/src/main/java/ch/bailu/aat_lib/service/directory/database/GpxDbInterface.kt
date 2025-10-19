package ch.bailu.aat_lib.service.directory.database

import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc
import java.io.Closeable

interface GpxDbInterface : Closeable {
    @Throws(DbException::class)
    fun deleteEntry(file: Foc)

    fun select(extraStatement: String, vararg params: Any): DbResultSet

    override fun close() {}

    companion object {
        val NULL_DATABASE: GpxDbInterface = object : GpxDbInterface {
            override fun deleteEntry(file: Foc) {}
            override fun select(extraStatement: String, vararg params: Any): DbResultSet {
                throw DbException("Not implemented")
            }
        }
    }
}
