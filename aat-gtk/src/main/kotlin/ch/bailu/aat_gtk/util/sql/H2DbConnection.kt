package ch.bailu.aat_gtk.util.sql

import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE

class H2DbConnection : DbConnection {
    private var connection: Connection? = null
    private val dbSuffix = ".mv.db"

    override fun open(name: String, version: Int, update: DbConnection.OnModelUpdate) {
        close()
        try {
            var dbName = name
            if (name.endsWith(dbSuffix)) {
                dbName = name.substring(0, name.length-dbSuffix.length)
            }
            connection = DriverManager.getConnection("jdbc:h2:${dbName}")

            if (getVersion() != version) {
                update.run(this)
                setVersion(version)
            }
        } catch (e: Exception) {
            throw DbException(e)
            close()
        }
    }

    private fun getVersion(): Int {
        var result = 0
        try {
            var res = query("SELECT * FROM version")
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
            var res = stmt.executeQuery()

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
        var connection = connection

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
