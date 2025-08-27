package ch.bailu.aat_lib.util.sql

interface DbResultSet {
    fun moveToFirst(): Boolean
    fun moveToNext(): Boolean
    fun moveToPrevious(): Boolean
    fun moveToPosition(pos: Int): Boolean

    fun getPosition(): Int
    fun getCount(): Int
    fun getString(column: String): String
    fun getLong(column: String): Long
    fun getFloat(column: String): Float
    fun isClosed(): Boolean

    fun close()
}
