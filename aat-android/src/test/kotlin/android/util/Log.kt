package android.util

import ch.bailu.aat.AppLogTest

/**
 * mock log functions
 */
object Log {
    fun d(tag: String, msg: String): Int {
        AppLogTest.logged = "DEBUG: $tag: $msg"
        println(AppLogTest.logged)
        return 0
    }

    fun i(tag: String, msg: String): Int {
        AppLogTest.logged = "INFO: $tag: $msg"
        println(AppLogTest.logged)
        return 0
    }

    fun w(tag: String, msg: String): Int {
        AppLogTest.logged = "WARN: $tag: $msg"
        println(AppLogTest.logged)
        return 0
    }

    fun e(tag: String, msg: String): Int {
        AppLogTest.logged = "ERROR: $tag: $msg"
        println(AppLogTest.logged)
        return 0
    }
}
