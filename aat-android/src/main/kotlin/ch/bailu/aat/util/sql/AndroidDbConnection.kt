package ch.bailu.aat.util.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import ch.bailu.aat_lib.resources.ToDo
import ch.bailu.aat_lib.service.directory.database.GpxDbConfiguration
import ch.bailu.aat_lib.util.sql.DbConnection
import ch.bailu.aat_lib.util.sql.DbException
import ch.bailu.aat_lib.util.sql.DbResultSet
import ch.bailu.aat_lib.util.sql.SaveDbResultSet
import ch.bailu.aat_lib.util.sql.Sql
import java.lang.IllegalArgumentException

class AndroidDbConnection(private val context: Context) : DbConnection {
    private var database: SQLiteDatabase? = null
    private var needsUpdate = false
    override fun open(name: String, version: Int) {
        try {
            close()
            database = OpenHelper(context, name, null, version).readableDatabase
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

    override fun execSQL(sql: String, vararg params: Any) {
        try {
            val database = this.database
            if (database != null) {
                if (params.isNotEmpty()) {
                    database.execSQL(sql, toStringArgs(params))
                } else {
                    database.execSQL(sql)
                }
            }
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    override fun query(sqlStatement: String, vararg params: Any): DbResultSet {
        try {
            val database = this.database

            if (database != null ) {
                val cursor = database.rawQuery(sqlStatement, toStringArgs(params))
                return SaveDbResultSet(AndroidDbResultSet(cursor))
            } else {
                throw DbException(ToDo.translate("No database"))
            }
        } catch (e1: IllegalArgumentException) {
            throw DbException(ToDo.translate("No files"))
        } catch (e: Exception) {
            throw DbException(e)
        }
    }

    private fun toStringArgs(vararg bindArgs: Any): Array<String?> {
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
