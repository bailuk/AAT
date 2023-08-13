package ch.bailu.aat_lib.util.sql

interface DbConnection {
    @Throws(DbException::class)
    fun open(name: String, version: Int)

    @Throws(DbException::class)
    fun execSQL(sql: String, vararg params: Any)

    @Throws(DbException::class)
    fun query(sqlStatement: String, vararg params: Any): DbResultSet
    fun close()
}
