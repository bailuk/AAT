package ch.bailu.aat.util.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.aat_lib.util.sql.SaveDbResultSet
import ch.bailu.aat_lib.util.sql.Sql

class AndroidDbConnection(private val context: Context) : DbConnection {
    private var database: SQLiteDatabase? = null
    private var needsUpdate = false
    override fun open(path: String, version: Int) {
        try {
            close()
            database = OpenHelper(context, path, null, version).readableDatabase
            if (needsUpdate) {
                createTable()
                needsUpdate = false
            }
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    private fun createTable() {
        execSQL(Sql.getTableDropStatement(GpxDbConfiguration.TABLE))
        execSQL(
            Sql.getCreateTableExpression(
                GpxDbConfiguration.TABLE,
                GpxDbConfiguration.KEY_LIST,
                GpxDbConfiguration.TYPE_LIST
            )
        )
    }

    override fun execSQL(sql: String, vararg bindArgs: Any) {
        try {
            val database = this.database
            if (database != null) {
                if (bindArgs.isNotEmpty()) {
                    database.execSQL(sql, toStringArgs(bindArgs as Array<Any>))
                } else {
                    database.execSQL(sql)
                }
            }
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    override fun query(sql: String, vararg bindArgs: Any): DbResultSet? {
        return try {
            val database = this.database
            if (database != null) {
                val cursor = database.rawQuery(sql, toStringArgs(bindArgs as Array<Any>))
                return SaveDbResultSet(AndroidDbResultSet(cursor))
            }
            null
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    private fun toStringArgs(bindArgs: Array<Any>): Array<String?> {
        val res = arrayOfNulls<String>(bindArgs.size)
        var index = 0
        for (o in bindArgs) {
            res[index++] = o.toString()
        }
        return res
    }

    override fun close() {
        try {
            val database = this.database
            if (database != null) {
                database.close()
                this.database = null
            }
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    private inner class OpenHelper(
        context: Context?,
        name: String?,
        factory: CursorFactory?,
        version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase) {
            needsUpdate = true
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            needsUpdate = true
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            needsUpdate = true
        }
    }
}
