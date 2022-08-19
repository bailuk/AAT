package ch.bailu.aat_gtk.util.sql

import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.aat_lib.util.sql.Sql
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE

class H2DbConnection : DbConnection {
    private var connection: Connection? = null
    private val dbSuffix = ".mv.db"

    override fun open(name: String, version: Int) {
        close()
        try {
            var dbName = name
            if (name.endsWith(dbSuffix)) {
                dbName = name.substring(0, name.length-dbSuffix.length)
            }
            connection = DriverManager.getConnection("jdbc:h2:${dbName}")

            if (getVersion() != version) {
                createTable()
                setVersion(version)
            }
        } catch (e: Exception) {
            close()
            throw DbException(e)
        }
    }

    private fun createTable() {
        execSQL(Sql.getTableDropStatement(GpxDbConfiguration.TABLE))
        execSQL(
            Sql.getCreateTableExpression(
                GpxDbConfiguration.TABLE,
                GpxDbConfiguration.KEY_LIST,
                GpxDbConfiguration.TYPE_LIST_H2
            )
        )
    }

    private fun getVersion(): Int {
        var result: Int
        try {
            val res = query("SELECT * FROM version")
            result = res.getLong("version").toInt()
            res.close()
        } catch (e: Exception) {
            result = 0
        }
        return result
    }

    private fun setVersion(version: Int) {
        execSQL("DROP TABLE version IF EXISTS")
        execSQL("CREATE TABLE version (version INT)")
        execSQL("INSERT INTO version (version) VALUES (${version})")
    }

    override fun execSQL(sqlStatement: String, vararg params: Any?) {
        try {
            // println(sqlStatement)

            val stmt = getPreparedStatement(sqlStatement, params)
            stmt.execute()
            stmt.close()
        } catch (e: Exception) {
             throw DbException(e)
        }
    }


    override fun query(sqlStatement: String, vararg params: Any?): DbResultSet {
        try {
            //println(sqlStatement)

            val stmt = getPreparedStatement(sqlStatement, params)
            val res = stmt.executeQuery()

            //stmt.close()
            return ScrollInsensitiveResultSet(res)
        } catch (e: Exception) {
            throw DbException(e)
        }
    }


    override fun close() {
        connection?.close()
        connection = null
    }


     private fun getPreparedStatement(sqlStatement: String, params: Array<out Any?>) : PreparedStatement {
        val connection = connection

        if (connection != null) {
            val stmt = connection.prepareStatement(sqlStatement, TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY)

            for ((i, p) in params.withIndex()) {
                val index = i + 1
                if (p is Int) {
                    stmt.setInt(index, p)
                } else if (p is String) {
                    stmt.setString(index, p)
                } else if (p is Long) {
                    stmt.setLong(index, p)
                } else if (p is Float) {
                    stmt.setFloat(index, p)
                }
            }
            return stmt
        }
        throw DbException("No connection")
    }
}
