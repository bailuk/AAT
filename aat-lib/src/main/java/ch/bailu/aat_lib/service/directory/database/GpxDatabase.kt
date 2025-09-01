package ch.bailu.aat_lib.service.directory.database

import ch.bailu.aat_lib.util.sql.DbConnectionInterface
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

class GpxDatabase (
    private val database: DbConnectionInterface,
    path: String,
    attributeList: Array<String> = GpxDbConfiguration.ATTR_LIST
) : GpxDbInterface {

    private val selectAll = "SELECT ${join(attributeList)} FROM ${GpxDbConfiguration.TABLE}"

    init {
        this.database.open(path, GpxDbConfiguration.DB_VERSION)
    }

    @Throws(DbException::class)
    override fun close() {
        database.close()
    }

    override fun select(extraStatement: String, vararg params: Any): DbResultSet {
        return database.query("$selectAll $extraStatement", *params)
    }

    @Throws(DbException::class)
    override fun deleteEntry(file: Foc) {
        database.execSQL(
            "DELETE FROM " + GpxDbConfiguration.TABLE + " WHERE " + GpxDbConfiguration.ATTR_FILENAME + " = ?",
            file.name
        )
    }

    fun insert(keys: Array<String>, vararg values: Any) {
        database.execSQL(
            "INSERT INTO " + GpxDbConfiguration.TABLE + " ( " + join(keys) + ") VALUES ( " + repeat(
                "?",
                values.size
            ) + " )", *values
        )
    }

    companion object {
        private fun repeat(what: String, times: Int): String {
            val builder = StringBuilder()
            var del = ""
            for (i in 0 until times) {
                builder.append(del)
                builder.append(what)
                del = ", "
            }
            return builder.toString()
        }

        private fun join(keys: Array<String>): String {
            val builder = StringBuilder()
            var del = ""
            for (k in keys) {
                builder.append(del)
                builder.append(k)
                del = ", "
            }
            return builder.toString()
        }
    }
}
