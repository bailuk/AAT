package ch.bailu.aat_lib.util.sql

class DbException : RuntimeException {
    constructor(msg: String) : super(msg)
    constructor(e: Exception) : super(e)
}
