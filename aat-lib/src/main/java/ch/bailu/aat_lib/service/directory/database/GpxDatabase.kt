package ch.bailu.aat_lib.service.directory.database

import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.foc.Foc

class GpxDatabase @JvmOverloads constructor(
    database: DbConnection,
    path: String,
    private val keys: Array<String> = GpxDbConfiguration.KEY_LIST
) : AbsDatabase() {
    private val database: DbConnection

    init {
        this.database = database
        this.database.open(path, GpxDbConfiguration.DB_VERSION)
    }

    override fun query(selection: String?): DbResultSet {
        return database.query(
            "SELECT " + join(keys) + " FROM " + GpxDbConfiguration.TABLE + where(
                selection
            ) + " ORDER BY " + GpxDbConfiguration.KEY_START_TIME + " DESC"
        )
    }

    override fun close() {
        database.close()
    }

    @Throws(DbException::class)
    override fun deleteEntry(file: Foc) {
        database.execSQL(
            "DELETE FROM " + GpxDbConfiguration.TABLE + " WHERE " + GpxDbConfiguration.KEY_FILENAME + " = ?",
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
        private fun where(selection: String?): String {
            return if (selection != null && selection.length > 3) {
                " WHERE $selection"
            } else ""
        }

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
